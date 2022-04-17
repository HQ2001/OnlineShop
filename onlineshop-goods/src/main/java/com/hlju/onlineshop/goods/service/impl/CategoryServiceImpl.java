package com.hlju.onlineshop.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hlju.onlineshop.goods.dao.CategoryBrandRelationDao;
import com.hlju.onlineshop.goods.vo.Catalog2VO;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.CategoryDao;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    private final CategoryBrandRelationDao categoryBrandRelationDao;
    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;

    @Autowired
    public CategoryServiceImpl(CategoryBrandRelationDao categoryBrandRelationDao,
                               StringRedisTemplate redisTemplate,
                               RedissonClient redissonClient) {
        this.categoryBrandRelationDao = categoryBrandRelationDao;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listByTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> level1List = entities.stream()
                .filter(item -> 1 == item.getCatLevel())
                .collect(Collectors.toList());
        // List<CategoryEntity> level2List = entities.stream()
        //         .filter(item -> 2 == item.getCatLevel())
        //         .collect(Collectors.toList());
        // List<CategoryEntity> level3List = entities.stream()
        //         .filter(item -> 3 == item.getCatLevel())
        //         .collect(Collectors.toList());
        // level1List.forEach(parent -> {
        //     List<CategoryEntity> children = level2List.stream()
        //             .filter(child -> parent.getCatId().equals(child.getParentCid()))
        //             .collect(Collectors.toList());
        //     parent.setChildren(children);
        // });
        // level2List.forEach(parent -> {
        //     List<CategoryEntity> children = level3List.stream()
        //             .filter(child -> parent.getCatId().equals(child.getParentCid()))
        //             .collect(Collectors.toList());
        //     parent.setChildren(children);
        // });
        return level1List.stream()
                .peek(menu -> menu.setChildren(this.getChildren(menu, entities)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Cacheable(value = {"categories"}, key = "#root.methodName+#root.args")
    @Override
    public List<CategoryEntity> listByIds(List<Long> categoryIds) {
        return baseMapper.listByIds(categoryIds);
    }

    /**
     * 获取子分类
     *
     * @param currentCategory 当前分类
     * @param allCategory     所有分类的列表
     * @return 子分类
     */
    private List<CategoryEntity> getChildren(CategoryEntity currentCategory, List<CategoryEntity> allCategory) {
        return allCategory.stream()
                .filter(category -> currentCategory.getCatId().equals(category.getParentCid()))
                .peek(menu -> menu.setChildren(this.getChildren(menu, allCategory)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        // TODO 检查删除的菜单是否被别的地方引用
        baseMapper.deleteBatchIds(catIds);
    }

    @Override
    public List<Long> getCategoryPathById(Long catId) {
        List<Long> path = Lists.newArrayList();

        findParentPath(catId, path);

        Collections.reverse(path);
        return path;
    }

    private void findParentPath(Long catId, List<Long> path) {
        CategoryEntity category = baseMapper.selectById(catId);
        path.add(catId);
        Long parentCid = category.getParentCid();
        if (parentCid != 0) {
            findParentPath(category.getParentCid(), path);
        }
    }

    @Transactional
    @Override
    @CacheEvict(value = "category", allEntries = true)
    public void updateDetail(CategoryEntity category) {
        baseMapper.updateById(category);
        categoryBrandRelationDao.updateCategory(category);
    }

    @Cacheable(value = {"category"}, key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categories(int level) {
        return baseMapper.listByCateLevel(level);
    }

    @Cacheable(value = {"category"}, key = "#root.methodName", sync = true)
    @Override
    public Map<String, List<Catalog2VO>> getCatalogJson() {
        return this.getCatalogFromDb();
    }

    // 保证了只查一次库（redisson）
    public Map<String, List<Catalog2VO>> getCatalogJson2() {
        // 从缓存中获取
        String categoryJsonKey = "goods.categoryJson";
        String categoryJson = redisTemplate.opsForValue().get(categoryJsonKey);
        if (StringUtils.isNotEmpty(categoryJson)) {
            TypeReference<Map<String, List<Catalog2VO>>> typeReference =
                    new TypeReference<Map<String, List<Catalog2VO>>>() {
                    };
            return JSON.parseObject(categoryJson, typeReference);
        }

        // 用分布式锁解决高并发下多次查库
        String categoryJsonLockKey = "goods.categoryJson.lock";
        RLock lock = redissonClient.getLock(categoryJsonLockKey);
        boolean getLockSuccess = lock.tryLock();
        if (getLockSuccess) {
            lock.lock(30, TimeUnit.SECONDS);
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.getCatalogJson();
        }

        Map<String, List<Catalog2VO>> map;
        try {
            map = this.getCatalogFromDb();
            // 保存到redis中
            redisTemplate.opsForValue().set(categoryJsonKey, JSON.toJSONString(map), 1, TimeUnit.DAYS);
        } finally {
            // 使用lua脚本保证删锁原子操作
            lock.unlock();
        }

        return map;
    }

    private Map<String, List<Catalog2VO>> getCatalogFromDb() {
        log.debug("查询数据库");
        Map<String, List<Catalog2VO>> map = new HashMap<>();
        List<CategoryEntity> categoryEntities = this.listByTree();
        categoryEntities.forEach(category -> {
            List<Catalog2VO> catalog2VOS = new ArrayList<>();
            for (CategoryEntity category2 : category.getChildren()) {
                Catalog2VO catalog2VO = new Catalog2VO();
                catalog2VO.setCatalog1Id(category2.getParentCid().toString());
                catalog2VO.setId(category2.getCatId().toString());
                catalog2VO.setName(category2.getName());
                List<Catalog2VO.Catalog3VO> catalog3List = new ArrayList<>();
                for (CategoryEntity category3 : category2.getChildren()) {
                    Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO();
                    catalog3VO.setId(category3.getCatId().toString());
                    catalog3VO.setName(category3.getName());
                    catalog3VO.setCatalog2Id(category3.getParentCid().toString());
                    catalog3List.add(catalog3VO);
                }
                catalog2VO.setCatalog3List(catalog3List);
                catalog2VOS.add(catalog2VO);
            }
            map.put(category.getCatId().toString(), catalog2VOS);
        });
        return map;
    }

}