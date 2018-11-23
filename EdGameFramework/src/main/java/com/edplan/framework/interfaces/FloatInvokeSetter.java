package com.edplan.framework.interfaces;

@FunctionalInterface
public interface FloatInvokeSetter<T> {
    void invoke(T target, float v);
}
