package com.edplan.framework.utils.functionality;

@FunctionalInterface
public interface Operation<T> {
    T op(T t);
}
