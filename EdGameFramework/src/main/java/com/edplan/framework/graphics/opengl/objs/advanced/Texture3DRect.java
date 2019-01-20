package com.edplan.framework.graphics.opengl.objs.advanced;

import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.objs.VertexList;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;

@Deprecated
public class Texture3DRect implements VertexList<TextureVertex3D> {
    private TextureVertex3D[] v = new TextureVertex3D[4];

    private RectF drawRect;

    private RectF textureRect;

    private GLPaint paint;

    public Texture3DRect() {

    }

    public Texture3DRect setDrawRect(RectF drawRect) {
        this.drawRect = drawRect;
        return this;
    }

    public RectF getDrawRect() {
        return drawRect;
    }

    public Texture3DRect setTextureRect(RectF textureRect) {
        this.textureRect = textureRect;
        return this;
    }

    public RectF getTextureRect() {
        return textureRect;
    }

    public Texture3DRect setPaint(GLPaint paint) {
        this.paint = paint;
        return this;
    }

    public GLPaint getPaint() {
        return paint;
    }


    private float defZ = 0;

    public Texture3DRect make() {
        v[0] = TextureVertex3D.atPosition(new Vec3(drawRect.getLeft(), drawRect.getTop(), defZ))
                .setTexturePoint(new Vec2(textureRect.getLeft(), textureRect.getTop()))
                .setColor(paint.getVaryingColor());
        v[1] = TextureVertex3D.atPosition(new Vec3(drawRect.getRight(), drawRect.getTop(), defZ))
                .setTexturePoint(new Vec2(textureRect.getRight(), textureRect.getTop()))
                .setColor(paint.getVaryingColor());
        v[2] = TextureVertex3D.atPosition(new Vec3(drawRect.getRight(), drawRect.getBottom(), defZ))
                .setTexturePoint(new Vec2(textureRect.getRight(), textureRect.getBottom()))
                .setColor(paint.getVaryingColor());
        v[3] = TextureVertex3D.atPosition(new Vec3(drawRect.getLeft(), drawRect.getBottom(), defZ))
                .setTexturePoint(new Vec2(textureRect.getLeft(), textureRect.getBottom()))
                .setColor(paint.getVaryingColor());
        return this;
    }

    public TextureVertex3D[] rawVertex() {
        return v;
    }

    @Override
    public TextureVertex3D[] listVertex() {

        return new TextureVertex3D[]{v[0], v[1], v[2], v[0], v[2], v[3]};
    }

    public static Texture3DRect makeup(RectF drawRect, RectF textureRect, GLPaint paint) {
        return
                new Texture3DRect()
                        .setDrawRect(drawRect)
                        .setTextureRect(textureRect)
                        .setPaint(paint).make();
    }

}
