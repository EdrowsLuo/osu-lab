package com.edplan.framework.utils;


/**
 * 单纯的操作一个对象
 * @param <T> 任何对象
 */

@FunctionalInterface
public interface Operation<T> {
    void operate(T target);
}
