package com.edplan.framework.utils;

public abstract class Lazy<T> {
    private T data;

    protected abstract T initial();

    public T get() {
        if (data == null) {
            data = initial();
        }
        return data;
    }
}
