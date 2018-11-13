package com.edplan.framework.utils;

@FunctionalInterface
public interface EmptyConstructor<T> {
    T create();
}
