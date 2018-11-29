package com.edplan.framework.utils.interfaces;

public interface InvokeSetter<T, V> {
    void invoke(T target, V value);
}
