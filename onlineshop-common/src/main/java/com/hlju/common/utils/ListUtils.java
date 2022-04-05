package com.hlju.common.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/27 10:58
 */
public class ListUtils {


    /**
     * 根据排序字段进行排序
     *
     * @param list           要排序的list
     * @param sortedFunction 排序方法（调用lambda方法）
     * @param <T>            list的类型
     * @param <U>            排序字段的类型
     * @return 排序后的list
     */
    public static <T, U extends Comparable<U>> List<T> sortList(List<T> list, Function<T, U> sortedFunction) {
        return list.stream()
                .sorted(Comparator.comparing(sortedFunction))
                .collect(Collectors.toList());
    }

}
