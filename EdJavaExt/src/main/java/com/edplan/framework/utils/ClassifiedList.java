package com.edplan.framework.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 将一些对象存储起来，然后快速的获取某类对象的遍历器以及快速的改变对象所在类
 * 特殊情况：type为-1表示不参与分类，遍历type为-1的对象等价于遍历所有对象
 * @param <T>
 */
public class ClassifiedList<T> implements Iterable<T> {

    private LinkedList<Node> allNodes = new LinkedList<>();

    private ArrayList<Node> classifiedLinkNodesFirst = new ArrayList<>();

    /**
     * 添加一个对象并返回控制这个对象的Node
     * @param object 新添加的对象
     * @return 控制这个对象的Node
     */
    public Node add(T object) {
        Node node = new Node(allNodes.size(), object);
        allNodes.addLast(node);
        return node;
    }

    /**
     * 获取对应type的第一个Node
     * @param type type值
     * @return 对应type的第一个Node
     */
    private Node getFirst(int type) {
        while (classifiedLinkNodesFirst.size() <= type) {
            classifiedLinkNodesFirst.add(null);
        }
        return classifiedLinkNodesFirst.get(type);
    }

    /**
     * 返回type对应的对象的遍历器，如果type为-1则返回全部对象的遍历器
     * @param type 对应的type
     * @return 对应的遍历器
     */
    public Iterator<Node> iteratorByType(int type) {
        if (type == -1) {
            return allNodes.iterator();
        } else {
            return new Iterator<Node>() {

                private Node node = getFirst(type);

                @Override
                public boolean hasNext() {
                    return node != null;
                }

                @Override
                public Node next() {
                    Node tmp = node;
                    node = node.next;
                    return tmp;
                }
            };
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Iterator<Node> iterator = allNodes.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next().object;
            }
        };
    }

    public Iterable<T> getAll(int type) {
        if (type == -1) {
            return this;
        } else {
            return () -> new Iterator<T>() {
                private Node node = getFirst(type);

                @Override
                public boolean hasNext() {
                    return node != null;
                }

                @Override
                public T next() {
                    Node tmp = node;
                    node = node.next;
                    return tmp.object;
                }
            };
        }
    }

    public void clear() {
        allNodes.clear();
        classifiedLinkNodesFirst.clear();
    }

    public class Node {

        private int type = -1;

        private final int idx;

        private T object;

        private Node next,pre;

        public Node(int idx, T object) {
            this.idx = idx;
            this.object = object;
        }

        public T getObject() {
            return object;
        }

        public void changeType(int type) {
            if (type == this.type) {
                return;
            }
            unlink();
            if (type != -1) {
                Node node = getFirst(type);
                if (node == null) {
                    classifiedLinkNodesFirst.set(type, this);
                } else {
                    while (node.idx < idx) {
                        if (node.next == null) {
                            node.next = this;
                            pre = node;
                            return;
                        } else {
                            node = node.next;
                        }
                    }
                    if (node.pre != null) {
                        pre = node.pre;
                        pre.next = this;
                    }
                    next = node;
                    node.pre = this;
                }
            }
        }

        private void unlink() {
            if (next != null) {
                next.pre = pre;
            }
            if (pre != null) {
                pre.next = next;
            } else {
                classifiedLinkNodesFirst.set(type, next);
            }
            pre = null;
            next = null;
        }

        public int getType() {
            return type;
        }
    }
}
