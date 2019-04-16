package com.edplan.framework.utils.interfaces;

@FunctionalInterface
public interface Function2Args<T,K,L> {
    L reflect(T t, K k);
}
