package com.edplan.framework.ui.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.drawable.sprite.RoundedRectSprite;

public class RoundedRectDrawable extends EdDrawable {
    private RoundedRectSprite sprite;

    public RoundedRectDrawable(MContext c) {
        super(c);
        sprite = new RoundedRectSprite(c);
        sprite.setTexture(GLTexture.White);
    }

    public void setColor(Color4 c) {
        sprite.setAccentColor(c);
    }

    public void setRadius(float r) {
        sprite.setRoundedRadius(r);
    }

    @Override
    public void draw(BaseCanvas canvas) {
        sprite.setArea(RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()));
        sprite.setRect(0, 0, canvas.getWidth(), canvas.getHeight());
        sprite.draw(canvas);
    }
}
