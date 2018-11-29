package com.edplan.framework.utils.interfaces;

@FunctionalInterface
public interface FloatInvokeSetter<T> {
    void invoke(T target, float v);
}
