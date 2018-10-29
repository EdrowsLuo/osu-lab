package com.edplan.framework.ui.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;

public class RectDrawable extends EdDrawable {
    private GLPaint paint = new GLPaint();

    public RectDrawable(MContext c) {
        super(c);
    }

    public void setColor(Color4 c) {
        paint.setMixColor(c);
    }

    @Override
    public void draw(BaseCanvas canvas) {

        canvas.drawTexture(GLTexture.ErrorTexture, RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }
}
