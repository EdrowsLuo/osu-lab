package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Function2Args;

import java.util.Iterator;

public class Iterators {

    public static <T> Iterator<T> iterator(T[] list, int offset) {
        return iterator(list, offset, list.length - offset);
    }

    public static <T> Iterator<T> iterator(T[] list, int offset, int length) {
        return new Iterator<T>() {

            int idx = offset;

            int end = offset + length;

            @Override
            public boolean hasNext() {
                return idx < end;
            }

            @Override
            public T next() {
                return list[idx++];
            }

        };
    }

    public static <T, K, L> Iterator<L> join(Iterator<T> t, Iterator<K> k, Function2Args<T, K, L> function) {
        return new Iterator<L>() {
            @Override
            public boolean hasNext() {
                return t.hasNext() && k.hasNext();
            }

            @Override
            public L next() {
                return function.reflect(t.next(), k.next());
            }
        };
    }

}
