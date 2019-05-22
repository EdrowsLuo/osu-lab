package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Getter;

/**
 * 当一个成员变量是不一定使用或者不需要在初始化时初始化的时候可以用Lazy来包装
 * @param <T>
 */
public abstract class Lazy<T> {
    private T data;

    protected abstract T initial();

    public T get() {
        if (data == null) {
            data = initial();
        }
        return data;
    }

    public boolean isEmpty() {
        return data == null;
    }

    public static <T> Lazy<T> create(Getter<T> getter) {
        return new Lazy<T>() {
            @Override
            protected T initial() {
                return getter.get();
            }
        };
    }
}
