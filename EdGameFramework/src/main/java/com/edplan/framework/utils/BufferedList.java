package com.edplan.framework.utils;

import com.edplan.framework.interfaces.Setable;

import java.util.ArrayList;

import com.edplan.framework.interfaces.ArrayConstructor;

import java.util.Iterator;

public class BufferedList<T extends Setable> implements Iterable<T> {
    private T[] ary;
    private int index = 0;
    private Constructor<T> constructor;
    private ArrayConstructor<T> arrayConstructor;

    public BufferedList(int initialSize, Constructor<T> constructor, ArrayConstructor<T> arrayConstructor) {
        this.constructor = constructor;
        this.arrayConstructor = arrayConstructor;
        ary = arrayConstructor.create(initialSize);
        for (int i = 0; i < initialSize; i++) {
            ary[i] = constructor.createNewObject();
        }
    }

    private void expand(int size) {
        T[] pre = ary;
        int prel = (pre != null) ? (pre.length) : 0;
        if (size <= prel) return;
        ary = arrayConstructor.create(size);
        for (int i = 0; i < prel; i++) {
            ary[i] = pre[i];
        }
        for (int i = prel; i < size; i++) {
            ary[i] = constructor.createNewObject();
        }
    }

    public void add(T e) {

        if (index < ary.length) {
            ary[index].set(e);
            index++;
        } else {
            expand(ary.length * 3 / 2 + 2);
            add(e);
        }
    }

    public T get(int i) {
        return ary[i];
    }

    public int size() {
        return index;
    }

    public void clear() {
        index = 0;
    }

    public void dispos() {
        for (int i = 0; i < ary.length; i++) {
            ary[i] = null;
        }
        ary = null;
    }

    @Override
    public Iterator<T> iterator() {

        return new Iter();
    }

    private class Iter implements Iterator<T> {
        int idx = 0;

        @Override
        public boolean hasNext() {

            return idx < index;
        }

        @Override
        public T next() {

            idx++;
            return ary[idx - 1];
        }

        @Override
        public void remove() {

        }
    }
}
