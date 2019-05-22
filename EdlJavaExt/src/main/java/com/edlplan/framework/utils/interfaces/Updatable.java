package com.edlplan.framework.utils.interfaces;

@FunctionalInterface
public interface Updatable<T> {
    void update(T t);
}
