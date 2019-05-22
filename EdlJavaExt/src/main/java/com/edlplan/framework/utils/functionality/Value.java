package com.edlplan.framework.utils.functionality;

@FunctionalInterface
public interface Value<T> {
    T get();

    default Value<T> op(Operation<T> operation) {
        return () -> operation.op(get());
    }

    default Value<T> flat() {
        T t = get();
        return () -> t;
    }

    default <V> Value<V> map(Map<T, V> map) {
        return () -> map.map(get());
    }

    static <T> Value<T> of(T t) {
        return () -> t;
    }
}
