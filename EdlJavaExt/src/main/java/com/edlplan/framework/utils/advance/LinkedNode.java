package com.edlplan.framework.utils.advance;

public final class LinkedNode<T> {

    public LinkedNode<T> pre;

    public LinkedNode<T> next;

    public T value;

    /**
     *
     */
    private void checkLeft() {
        if (pre != null) {
            pre.next = this;
        }
    }

    /**
     *
     */
    private void checkRight() {
        if (next != null) {
            next.pre = this;
        }
    }

    public void removeFromList() {
        if (pre != null) {
            pre.next = next;
        }
        if (next != null) {
            next.pre = pre;
        }
        pre = null;
        next = null;
    }

    public void insertToNext(LinkedNode<T> node) {
        if (next != null) {
            next.pre = node;
        }
        node.next = next;
        next = node;
        node.pre = this;
    }

    public void insertToPrevious(LinkedNode<T> node) {
        if (pre != null) {
            pre.next = node;
        }
        node.pre = pre;
        pre = node;
        node.next = this;
    }


}
