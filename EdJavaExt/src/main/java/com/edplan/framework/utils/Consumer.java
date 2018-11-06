package com.edplan.framework.utils;

@FunctionalInterface
public interface Consumer<T> {
    void consume(T v);
}
