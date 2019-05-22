package com.edlplan.framework.graphics.opengl.buffer.direct;

public interface DirectAttributeBuffer {
    public void ensureSize(int size);

    public void loadToAttribute();
}
