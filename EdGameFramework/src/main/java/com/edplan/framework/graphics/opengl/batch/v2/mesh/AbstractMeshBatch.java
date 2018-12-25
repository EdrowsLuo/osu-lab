package com.edplan.framework.graphics.opengl.batch.v2.mesh;

import com.edplan.framework.graphics.opengl.batch.v2.AbstractBatch;
import com.edplan.framework.utils.annotation.ReadOnly;

public abstract class AbstractMeshBatch<T extends Mesh> extends AbstractBatch<T> {

    protected final int maxBatch;

    @ReadOnly
    protected int currentSize;

    public AbstractMeshBatch(int maxBatch) {
        this.maxBatch = maxBatch;
    }

    protected abstract void appendData(T mesh, int offset, int size);

    protected abstract void clearBuffer();

    protected abstract void onApplyToGL();

    private final void add(T mesh, int offset, int size) {
        if (currentSize + size >= maxBatch) {
            flush();
        }
        appendData(mesh, offset, size);
        currentSize += size;
    }

    @Override
    protected final void clearData() {
        currentSize = 0;
        clearBuffer();
    }

    @Override
    protected void onBind() {

    }

    @Override
    protected void onUnbind() {

    }

    @Override
    public final void add(T mesh) {
        if (!isBind()) {
            bind();
        }
        if (mesh.size() < maxBatch) {
            add(mesh, 0, mesh.size());
        } else {
            int size = mesh.size();
            int offset = 0;
            int postSize;
            while (size != 0) {
                postSize = Math.min(maxBatch, size);
                add(mesh, offset, postSize);
                offset += postSize;
                size -= postSize;
            }
        }
    }

    @Override
    protected final void applyToGL() {
        if (currentSize != 0) {
            onApplyToGL();
        }
    }
}
