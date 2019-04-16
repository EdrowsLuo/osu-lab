package com.edplan.framework.utils.interfaces;

@FunctionalInterface
public interface Updatable<T> {
    void update(T t);
}
