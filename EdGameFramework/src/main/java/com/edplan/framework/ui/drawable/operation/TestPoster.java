package com.edplan.framework.ui.drawable.operation;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;

public class TestPoster implements ITexturePoster {
    @Override
    public void drawTexture(BaseCanvas canvas, GLTexture t) {
        GLPaint paint = new GLPaint();
        canvas.drawTexture(t, new RectF(0, 0, t.getWidth(), t.getHeight()), paint);
    }
}
