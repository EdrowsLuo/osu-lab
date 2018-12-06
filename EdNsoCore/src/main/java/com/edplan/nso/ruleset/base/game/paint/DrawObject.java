package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.utils.Lazy;
import com.edplan.framework.utils.interfaces.Consumer;
import com.edplan.nso.ruleset.base.game.World;

import java.util.LinkedList;
import java.util.List;

public abstract class DrawObject {

    DrawNode node;

    private final Lazy<LinkedList<Consumer<DrawObject>>> operations = Lazy.create(LinkedList::new);

    protected void handleOperations() {
        synchronized (operations) {
            if (!operations.isEmpty()) {
                for (Consumer<DrawObject> operation : operations.get()) {
                    operation.consume(this);
                }
                operations.get().clear();
            }
        }
    }

    /**
     * 具体的绘制操作
     * @param canvas 画板
     * @param world 世界上下文
     */
    public abstract void draw(BaseCanvas canvas, World world);

    public void postOperation(Consumer<DrawObject> operation) {
        synchronized (operations) {
            operations.get().add(operation);
        }
    }

    public void detach() {
        if (node != null) {
            node.detachSelf();
            node = null;
        }
    }

    public boolean isAttached() {
        return node != null;
    }

    private static Lazy<DrawObject> invalideObject = Lazy.create(() -> new DrawObject() {
        @Override
        public void draw(BaseCanvas canvas, World world) {

        }

        @Override
        public void postOperation(Consumer<DrawObject> operation) {
            //super.postOperation(operation);
        }
    });

    public static DrawObject invalid() {
        return invalideObject.get();
    }
}
