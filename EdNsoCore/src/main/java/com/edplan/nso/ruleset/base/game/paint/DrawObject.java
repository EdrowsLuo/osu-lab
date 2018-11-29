package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.utils.Lazy;
import com.edplan.framework.utils.interfaces.Consumer;

import java.util.LinkedList;
import java.util.List;

public abstract class DrawObject {

    private final Lazy<List<Consumer<DrawObject>>> operations = Lazy.create(LinkedList::new);

    public void handleOperations() {
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
     * @param canvas 世界画板
     */
    public abstract void draw(WorldCanvas canvas);

    public void postOperation(Consumer<DrawObject> operation) {
        synchronized (operations) {
            operations.get().add(operation);
        }
    }

    /**
     * @return 是否是在使用的
     */
    public boolean isEnable() {
        return true;
    }


}
