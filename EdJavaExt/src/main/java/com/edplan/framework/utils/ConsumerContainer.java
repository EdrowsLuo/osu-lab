package com.edplan.framework.utils;

public class ConsumerContainer<T> {
    private T target;

    public ConsumerContainer(T target) {
        this.target = target;
    }

    public ConsumerContainer<T> then(Consumer<T> c) {
        c.consume(target);
        return this;
    }

}