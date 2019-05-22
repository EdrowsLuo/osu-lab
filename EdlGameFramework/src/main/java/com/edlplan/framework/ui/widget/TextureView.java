package com.edlplan.framework.ui.widget;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.drawable.sprite.TextureSprite;
import com.edlplan.framework.ui.layout.EdMeasureSpec;

public class TextureView extends EdView {
    private TextureSprite sprite;

    public TextureView(MContext c) {
        super(c);
        sprite = new TextureSprite(c);
    }

    public void setTexture(AbstractTexture t) {
        sprite.setTexture(t);
    }

    public void setBlendType(BlendType b) {
        sprite.setBlendType(b);
    }

    @Override
    protected void onMeasure(long widthSpec, long heightSpec) {

        super.onMeasure(widthSpec, heightSpec);
        int wm = EdMeasureSpec.getMode(widthSpec);
        int hm = EdMeasureSpec.getMode(heightSpec);

        if (wm == EdMeasureSpec.MODE_DEFINEDED && hm == EdMeasureSpec.MODE_DEFINEDED) {
            setMeasuredDimensition(EdMeasureSpec.getSize(widthSpec), EdMeasureSpec.getSize(heightSpec));
            return;
        }

        if (sprite.getTexture() == null) {
            setMeasuredDimensition(0, 0);
        }

        setMeasuredDimensition(EdMeasureSpec.getSize(widthSpec), EdMeasureSpec.getSize(heightSpec));
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {

        super.onDraw(canvas);
        if (sprite.getTexture() != null) {
            sprite.setAreaFitTexture(RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()));
            sprite.draw(canvas);
        }
    }

}
