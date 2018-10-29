package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.MContext;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Quad;
import com.edplan.framework.math.RectF;

public class ColorRectSprite extends RectSprite<ColorSpriteShader> {
    public ColorRectSprite(MContext c) {
        super(c);
    }

    public void setParallelogram(RectF raw, float angle) {
        float d = raw.getHeight() / FMath.tan(angle) / 2;
        setArea(new Quad(raw.getTopLeft().add(d, 0), raw.getTopRight().add(d, 0),
                raw.getBottomLeft().add(-d, 0), raw.getBottomRight().add(-d, 0)));
    }

    @Override
    protected ColorSpriteShader createShader() {

        return ColorSpriteShader.get();
    }
}

