package com.edplan.framework.graphics.opengl.objs.texture;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.RectI;
import com.edplan.framework.math.Vec2;

public class SwapXYTextureRegion extends AbstractTexture {
    private GLTexture texture;

    private RectI area = new RectI();

    public SwapXYTextureRegion(GLTexture t, RectI area) {
        this.texture = t;
        this.area.set(area);
    }

    public void setArea(RectI _area) {
        this.area.set(_area);
    }

    public RectI getArea() {
        return area;
    }

    @Override
    public IQuad getRawQuad() {

        return null;
    }

    @Override
    public Vec2 toTexturePosition(float x, float y) {
        return texture.toTexturePosition(area.getX1() + y, area.getY1() + x);
    }

    @Override
    public GLTexture getTexture() {
        return texture;
    }

    @Override
    public int getTextureId() {
        return getTexture().getTextureId();
    }

    @Override
    public int getWidth() {
        return area.getHeight();
    }

    @Override
    public int getHeight() {
        return area.getWidth();
    }
}
