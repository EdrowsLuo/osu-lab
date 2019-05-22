package com.edlplan.framework.utils.advance;

import com.edlplan.framework.utils.interfaces.Consumer;

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
