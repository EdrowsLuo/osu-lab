package com.edplan.framework.utils.interfaces;

@FunctionalInterface
public interface Consumer<T> {
    void consume(T v);
}
