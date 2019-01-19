package com.edplan.framework.utils.functionality;

@FunctionalInterface
public interface Map<K,V> {
    V map(K k);
}
