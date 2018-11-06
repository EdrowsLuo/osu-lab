package com.edplan.superutils.classes;

import java.util.ArrayList;

/**
 * 经测试，在非常频繁（每毫秒多次，多线程同时操作）的操作时会出bug，
 * 但是在应付一般的情况（3条线程同时进行添加，每ms大概5次操作）的时候还是比较稳定的
 * 这个类可以保证添加的物件顺序和添加顺序相同
 *
 *
 * ps: 我当年写的什么叽霸
 */

@Deprecated
public class SafeList<T> extends ArrayList<T> {
    private ArrayList<T> bufferList1;

    private ArrayList<T> bufferList2;

    private boolean iterating = false;

    private boolean iteratingBuffer1 = false;

    private boolean iteratingBuffer2 = false;

    public SafeList() {
        super();
        bufferList1 = new ArrayList<T>();
        bufferList2 = new ArrayList<T>();
    }

    @Override
    public boolean add(T e) {

        if (iterating) {
            return bufferList1.add(e);
        } else {
            if (iteratingBuffer1) {
                return bufferList2.add(e);
            } else if (iteratingBuffer2) {
                return bufferList1.add(e);
            } else {
                return super.add(e);
            }
        }
    }

    public void startIterate() {
        iterating = true;
    }

    public void endIterate() {
        iterating = false;
        while (bufferList1.size() != 0 || bufferList2.size() != 0) {
            iteratingBuffer1 = true;
            for (T t : bufferList1) {
                super.add(t);
            }
            bufferList1.clear();
            iteratingBuffer1 = false;
            iteratingBuffer2 = true;
            for (T t : bufferList2) {
                super.add(t);
            }
            bufferList2.clear();
            iteratingBuffer2 = false;
        }
    }
}
