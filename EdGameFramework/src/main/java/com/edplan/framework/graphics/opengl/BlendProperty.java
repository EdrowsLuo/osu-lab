package com.edplan.framework.graphics.opengl;

import android.opengl.GLES20;

import com.edplan.framework.interfaces.Copyable;

public class BlendProperty implements Copyable {

    public boolean enable = true;

    public BlendType blendType = BlendType.Normal;

    public BlendProperty() {

    }

    public BlendProperty(BlendProperty b) {
        set(b);
    }

    public BlendProperty(boolean e, BlendType t) {
        this.enable = e;
        this.blendType = t;
    }

    public void set(BlendProperty b) {
        this.enable = b.enable;
        this.blendType = b.blendType;
    }

    public void applyToGL() {
        if (enable) {
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(blendType.srcType, blendType.dstType);
        } else {
            GLES20.glDisable(GLES20.GL_BLEND);
        }
    }

    public boolean equals(boolean _enable, BlendType _blendType) {
        return this.enable == _enable && this.blendType == _blendType;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof BlendProperty) {
            BlendProperty b = (BlendProperty) obj;
            return (enable == b.enable) && (blendType == b.blendType);
        } else return false;
    }

    @Override
    public Copyable copy() {

        return new BlendProperty(this);
    }
}
