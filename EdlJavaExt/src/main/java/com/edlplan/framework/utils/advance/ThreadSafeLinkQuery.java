package com.edlplan.framework.utils.advance;

public class ThreadSafeLinkQuery<T> {
    private Node<T>[] data = new Node[2];

    private int count;

    public void add(T t) {
        synchronized (data) {
            count++;
            if (data[0] == null) {
                Node<T> n = new Node<T>();
                n.value = t;
                data[0] = n;
                data[1] = n;
            } else {
                Node<T> n = new Node<T>();
                n.value = t;
                data[1].next = n;
                data[1] = n;
            }
        }
    }

    public String getCurrentMsg() {
        return data[0] != null ? data[0].toString() : "null";
    }

    public T next() {
        synchronized (data) {
            if (data[0] == null) {
                return null;
            } else {
                count--;
                final T t = data[0].value;
                data[0] = data[0].next;
                return t;
            }
        }
    }

    public int getCount() {
        return count;
    }

    public static class Node<T> {
        public Node<T> next;
        public T value;

        @Override
        public String toString() {
            return (next != null ? "hasNext" : "noNext") + " " + value;
        }
    }
}
