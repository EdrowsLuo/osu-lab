package com.edplan.framework.graphics.opengl.batch;

@Deprecated
public interface BaseBatch<T> {

    public void add(T t);

    public void add(T... ts);

    public int getVertexCount();

    public void clear();
}
