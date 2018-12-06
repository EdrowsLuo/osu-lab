package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.nso.ruleset.base.game.World;

public class GroupDrawObject extends DrawObject {

    protected DrawNode preStartNode = new DrawNode(DrawObject.invalid());
    protected DrawNode afterEndNode = preStartNode.attachFront(DrawObject.invalid());

    private boolean drawForward = true;

    @Override
    public void draw(BaseCanvas canvas, World world) {
        if (drawForward) {
            DrawNode nextNode = preStartNode.next;
            DrawObject object;
            while (nextNode != null) {
                object = nextNode.drawObject;
                nextNode = nextNode.next;
                object.draw(canvas, world);
            }
        } else {
            DrawNode nextNode = afterEndNode.pre;
            DrawObject object;
            while (nextNode != null) {
                object = nextNode.drawObject;
                nextNode = nextNode.pre;
                object.draw(canvas, world);
            }
        }
    }

    public void drawForward(boolean drawForward) {
        this.drawForward = drawForward;
    }

    public boolean isDrawForward() {
        return drawForward;
    }

    public void childrenAdd(DrawObject... objects) {
        for (DrawObject object : objects) {
            attachFront(object);
        }
    }

    public void attachBehind(DrawObject object) {
        if (object.node != null) {
            object.detach();
        }
        object.node = preStartNode.attachFront(object);
    }

    public void attachFront(DrawObject object) {
        if (object.node != null) {
            object.detach();
        }
        object.node = afterEndNode.attachBehind(object);
    }
}
