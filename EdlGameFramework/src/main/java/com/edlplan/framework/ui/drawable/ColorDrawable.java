package com.edlplan.framework.ui.drawable;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.drawable.sprite.ColorRectSprite;

public class ColorDrawable extends EdDrawable {
    private ColorRectSprite sprite;

    public ColorDrawable(MContext c) {
        super(c);
        sprite = new ColorRectSprite(c);
    }

    public void setAlpha(float a) {
        sprite.setAlpha(a);
    }

    public void setColor(Color4 c) {
        sprite.setAccentColor(c);
    }

    public void setColor(Color4 tl, Color4 tr, Color4 bl, Color4 br) {
        sprite.setColor(tl, tr, bl, br);
    }

    @Override
    public void draw(BaseCanvas canvas) {
        sprite.setArea(RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()));
        sprite.draw(canvas);
    }
}
