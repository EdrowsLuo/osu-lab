package com.edlplan.framework.utils.interfaces;

public interface InvokeSetter<T, V> {
    void invoke(T target, V value);
}
