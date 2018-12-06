package com.edplan.framework.graphics.opengl.batch.v2;

/**
 * 一个Batch表示缓存了一类的操作
 * @param <T>
 */
public abstract class AbstractBatch<T> {

    protected abstract void onBind();

    protected abstract void onUnbind();

    public abstract void add(T t);

    protected abstract void clearData();

    protected abstract void applyToGL();

    protected void checkForBind() {
        if (!isBind()) {
            bind();
        }
    }

    public final void bind() {
        BatchEngine.bind(this);
        onBind();
    }

    public final void unbind() {
        BatchEngine.unbind(this);
        onUnbind();
    }

    public final boolean isBind() {
        return BatchEngine.currentBatch() == this;
    }

    public final void flush() {
        applyToGL();
        clearData();
    }
}
