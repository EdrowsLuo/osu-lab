package com.edlplan.framework.utils.interfaces;

@FunctionalInterface
public interface Filter<T> {
    boolean check(T t);
}
