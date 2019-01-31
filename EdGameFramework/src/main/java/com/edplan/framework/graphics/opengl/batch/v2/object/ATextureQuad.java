package com.edplan.framework.graphics.opengl.batch.v2.object;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public abstract class ATextureQuad {

    public AbstractTexture texture;

    public abstract void write(float[] ary, int offset);
}
