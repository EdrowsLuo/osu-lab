package com.edlplan.framework.ui.drawable;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;

import java.lang.ref.WeakReference;

/**
 * 默认绘制在整个canvas上，
 * 默认不进行layout
 */
public abstract class EdDrawable {
//	private Mat4 translationMatrix=Mat4.createIdentity();

    private WeakReference<MContext> context;

    public EdDrawable(MContext context) {
        this.context = new WeakReference<MContext>(context);
    }

    public MContext getContext() {
        return context.get();
    }

    public abstract void draw(BaseCanvas canvas);

    public float getMinWidth() {
        return 0;
    }

    public float getMinHeight() {
        return 0;
    }
}
