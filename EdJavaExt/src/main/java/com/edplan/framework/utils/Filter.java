package com.edplan.framework.utils;

@FunctionalInterface
public interface Filter<T> {
    boolean check(T t);
}
