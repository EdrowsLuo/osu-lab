package com.edplan.framework.utils.functionality;

@FunctionalInterface
public interface Function<I, O> {
    O fun(I in);
}
