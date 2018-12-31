package com.edplan.framework.utils;

@FunctionalInterface
public interface Factory<T> {
    T create();
}
