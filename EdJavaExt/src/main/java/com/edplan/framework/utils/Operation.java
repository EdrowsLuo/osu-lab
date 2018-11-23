package com.edplan.framework.utils;

@FunctionalInterface
public interface Operation<T> {
    void operate(T target);
}
