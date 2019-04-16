package com.edplan.framework.graphics.opengl;

import android.opengl.GLES20;

public enum BlendType {
    Normal(
            GLES20.GL_ONE,
            GLES20.GL_ONE_MINUS_SRC_ALPHA
    ),
    Additive(
            GLES20.GL_ONE,
            GLES20.GL_ONE
    ),
    Delete(
            GLES20.GL_ZERO,
            GLES20.GL_ONE_MINUS_SRC_COLOR
    ),
    Delete_Alpha(
            GLES20.GL_ZERO,
            GLES20.GL_ONE_MINUS_SRC_ALPHA
    ),
    DeleteRepeat(
            GLES20.GL_ONE_MINUS_DST_ALPHA,
            GLES20.GL_ONE_MINUS_SRC_ALPHA
    )
    ;
    public final int srcType;
    public final int dstType;

    //public final boolean needPreMultiple;
    BlendType(int src, int dst) {
        srcType = src;
        dstType = dst;
        //needPreMultiple=prm;
    }
}
