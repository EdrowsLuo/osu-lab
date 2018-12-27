package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.Anchor;

public class FastTextureSprite extends BaseRectTextureSprite<FastTextureSpriteShader> {
    public FastTextureSprite(MContext c) {
        super(c);
    }

    @Override
    protected void prepareColorUniformBase(BaseCanvas canvas) {
        getShader().loadAccentColor(Color4.White);
        getShader().loadAlpha(alpha*canvas.getCanvasAlpha());
    }

    @Deprecated
    @Override
    public void setAccentColor(Color4 accentColor) {
        super.setAccentColor(accentColor);
    }

    @Deprecated
    @Override
    public void setColor(Color4 tl, Color4 tr, Color4 bl, Color4 br) {
        super.setColor(tl, tr, bl, br);
    }

    public void setAreaFitTexture(RectF rect) {
        float w = rect.getWidth() / getTexture().getWidth();
        float h = rect.getHeight() / getTexture().getHeight();
        if (w > h) {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getHeight() * getTexture().getWidth() / getTexture().getHeight(), rect.getHeight()));
        } else {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getWidth(), rect.getWidth() * getTexture().getHeight() / getTexture().getWidth()));
        }
    }

    public void setAreaFillTexture(RectF rect) {
        float w = rect.getWidth() / getTexture().getWidth();
        float h = rect.getHeight() / getTexture().getHeight();
        if (w < h) {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getHeight() * getTexture().getWidth() / getTexture().getHeight(), rect.getHeight()));
        } else {
            setArea(RectF.anchorOWH(Anchor.Center, rect.getCenterHorizon(), rect.getCenterVertical(), rect.getWidth(), rect.getWidth() * getTexture().getHeight() / getTexture().getWidth()));
        }
    }

    @Override
    protected FastTextureSpriteShader getShader() {
        return FastTextureSpriteShader.get();
    }
}
