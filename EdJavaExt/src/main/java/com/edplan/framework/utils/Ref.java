package com.edplan.framework.utils;

public class Ref<T> {
    T t;

    public Ref() {

    }

    public Ref(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }
}
