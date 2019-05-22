package com.edlplan.nso.ruleset.base.game.paint;

public class DrawNode {

    DrawNode next;

    DrawNode pre;

    DrawObject drawObject;

    public DrawNode(DrawObject object) {
        this.drawObject = object;
    }

    public DrawNode attachFront(DrawObject object) {
        final DrawNode node = new DrawNode(object);
        if (next != null) {
            next.pre = node;
        }
        node.next = next;
        node.pre = this;
        this.next = node;
        return node;
    }

    public DrawNode attachBehind(DrawObject object) {
        final DrawNode node = new DrawNode(object);
        if (pre != null) {
            pre.next = node;
        }
        node.next = this;
        node.pre = pre;
        this.pre = node;
        return node;
    }

    public void detachSelf() {
        if (pre != null) {
            pre.next = next;
        }
        if (next != null) {
            next.pre = pre;
        }
    }

}
