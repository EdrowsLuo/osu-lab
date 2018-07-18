package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.BlendType;

public abstract class Sprite<S extends SpriteShader> extends AbstractSprite {
    protected S shader;

    private float alpha = 1;

    private Color4 accentColor = Color4.ONE.copyNew();

    private BlendType blendType = BlendType.Normal;

    public Sprite(MContext c) {
        super(c);
        shader = createShader();
    }

    protected void setShader(S s) {
        shader = s;
    }

    public void setBlendType(BlendType blendType) {
        this.blendType = blendType;
    }

    public BlendType getBlendType() {
        return blendType;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAccentColor(Color4 accentColor) {
        this.accentColor.set(accentColor);
        this.accentColor.toPremultipledThis();
    }

    public Color4 getAccentColor() {
        return accentColor;
    }

    protected abstract S createShader();

    private boolean changeBlend = false;

    @Override
    protected void startDraw(BaseCanvas canvas) {

        shader.useThis();
        changeBlend = (canvas.getBlendSetting().getBlendType() == blendType);
        if (changeBlend) {
            canvas.getBlendSetting().save();
            canvas.getBlendSetting().setBlendType(blendType);
        }
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {
        shader.loadAccentColor(accentColor);
        shader.loadAlpha(alpha*canvas.getCanvasAlpha());
        shader.loadCamera(canvas.getCamera());
    }

    @Override
    protected void endDraw(BaseCanvas canvas) {

        if (changeBlend) {
            canvas.getBlendSetting().restore();
            changeBlend = false;
        }
    }
}
