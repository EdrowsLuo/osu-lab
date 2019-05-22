package com.edlplan.framework.utils.functionality;

@FunctionalInterface
public interface Map<K,V> {
    V map(K k);
}
