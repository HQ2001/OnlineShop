package io.renren.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
