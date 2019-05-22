package com.edplan.framework.utils;

public class DirectArray<T> {

    public T[] ary;

    public int length;

    public DirectArray(T[] ary) {
        this.ary = ary;
        for (int i = 0; i < ary.length; i++) {
            if (ary[i] == null) {
                break;
            }
            length++;
        }
    }

    public DirectArray(T[] ary, int length) {
        this.ary = ary;
        this.length = length;
    }

}
