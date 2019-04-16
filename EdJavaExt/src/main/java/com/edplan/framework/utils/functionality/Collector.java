package com.edplan.framework.utils.functionality;

import com.edplan.framework.utils.interfaces.Function;
import com.edplan.framework.utils.interfaces.Updatable;

public abstract class Collector<T, V> implements Updatable<T> {

    V value;

    public Collector(V v) {
        this.value = v;
    }

    public V getValue() {
        return value;
    }

    public static <T> Collector<T, Integer> max(int iniValue, Function<T, Integer> f) {
        return new Collector<T, Integer>(iniValue) {
            @Override
            public void update(T t) {
                int v = f.reflect(t);
                if (v > value) {
                    value = v;
                }
            }
        };
    }

    public static Collector<Integer, Integer> max(int iniValue) {
        return max(iniValue, Integer::intValue);
    }

}
