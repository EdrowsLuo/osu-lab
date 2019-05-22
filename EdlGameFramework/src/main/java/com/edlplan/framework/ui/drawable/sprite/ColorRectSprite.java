package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.MContext;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.math.Quad;

public class ColorRectSprite extends RectSprite<ColorSpriteShader> {
    public ColorRectSprite(MContext c) {
        super(c);
    }

    public void setParallelogram(RectF raw, float angle) {
        float d = (float) (raw.getHeight() / Math.tan(angle) / 2);
        setArea(new Quad(raw.getTopLeft().add(d, 0), raw.getTopRight().add(d, 0),
                raw.getBottomLeft().add(-d, 0), raw.getBottomRight().add(-d, 0)));
    }

    @Override
    protected ColorSpriteShader getShader() {
        return ColorSpriteShader.get();
    }
}

