package com.edplan.framework.ui.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.drawable.interfaces.IFadeable;
import com.edplan.framework.ui.drawable.interfaces.IRotateable2D;
import com.edplan.framework.ui.drawable.interfaces.IScaleable2D;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来封装将材质绘制到canvas上的Drawable，有类似canvas.drawTexture的方法
 */
public class TextureDrawable extends EdDrawable implements IFadeable, IScaleable2D, IRotateable2D {
    private float rotation;

    private Vec2 origin = new Vec2();

    private Vec2 baseSize = new Vec2();

    private GLPaint basePaint = new GLPaint();

    private Vec2 scale = new Vec2(1, 1);

    private List<BindTexture> textures = new ArrayList<BindTexture>();

    public TextureDrawable(MContext c) {
        super(c);
        initialPaint();
    }

    public void setOrigin(Vec2 origin) {
        this.origin = origin;
    }

    public Vec2 getOrigin() {
        return origin;
    }

    public void setBaseSize(Vec2 baseSize) {
        this.baseSize = baseSize;
    }

    public Vec2 getBaseSize() {
        return baseSize;
    }

    private void initialPaint() {

    }

    public void addTexture(BindTexture texture) {
        textures.add(texture);
    }

    public BindTexture addTexture(GLTexture texture) {
        BindTexture t = new BindTexture(texture);
        addTexture(t);
        return t;
    }

    @Override
    public void setRotation(float ang) {

        rotation = ang;
    }

    @Override
    public float getRotation() {

        return rotation;
    }

    @Override
    public void setAlpha(float a) {

        basePaint.setFinalAlpha(a);
    }

    @Override
    public float getAlpha() {

        return basePaint.getFinalAlpha();
    }

    @Override
    public void setScale(float sx, float sy) {

        scale.set(sx, sy);
    }

    @Override
    public Vec2 getScale() {

        return scale;
    }

    private Vec2 makeupVec(BindTexture t, float x, float y) {
        return origin.copy()
                .add(
                        (new Vec2(x, y))
                                .multiple(scale)
                                .multiple(t.getScale()));
    }

    private void drawBindTexture(BindTexture texture, BaseCanvas canvas) {
        Vec2 d0 = makeupVec(texture, -baseSize.x, -baseSize.y);
        Vec2 d1 = makeupVec(texture, baseSize.x, -baseSize.y);
        Vec2 d2 = makeupVec(texture, baseSize.x, baseSize.y);
        Vec2 d3 = makeupVec(texture, -baseSize.x, baseSize.y);
        Vec2 r0 = new Vec2(0, 0);
        Vec2 r1 = new Vec2(texture.getWidth(), 0);
        Vec2 r2 = new Vec2(texture.getWidth(), texture.getHeight());
        Vec2 r3 = new Vec2(0, texture.getHeight());
        canvas.drawTexture(
                texture.getTexture(),
                new Vec2[]{r0, r3, r1, r3, r2, r1},
                new Vec2[]{d0, d3, d1, d3, d2, d1},
                //basePaint.getColorMixRate(),
                basePaint.getVaryingColor(),
                basePaint.getFinalAlpha() * texture.getAlpha(),
                basePaint.getMixColor());
    }

    @Override
    public void draw(BaseCanvas canvas) {

        for (BindTexture t : textures) {
            drawBindTexture(t, canvas);
        }
    }
}
