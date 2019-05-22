package com.edlplan.framework.utils;

@FunctionalInterface
public interface Factory<T> {
    T create();
}
