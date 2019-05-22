package com.edlplan.framework.utils.functionality;

@FunctionalInterface
public interface Operation<T> {
    T op(T t);
}
