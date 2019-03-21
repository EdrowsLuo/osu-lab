package com.edplan.framework.graphics.opengl.objs.texture;

import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLES20;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Quad;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;

import java.nio.IntBuffer;

public class TextureRegion extends AbstractTexture {
    private GLTexture texture;

    private RectF area = new RectF();

    private Quad rawQuad;

    private int width, height;

    public TextureRegion(GLTexture t, RectF _area) {
        this.texture = t;
        setArea(_area);
    }

    public void setArea(RectF _area) {
        this.area.set(_area);
        updateArea();
    }

    public RectF getArea() {
        return area;
    }

    private void updateArea() {
        width = (int) area.getWidth();
        height = (int) area.getHeight();
        if (rawQuad == null) rawQuad = new Quad();
        rawQuad.set(
                toTexturePosition(0, 0),
                toTexturePosition(getWidth(), 0),
                toTexturePosition(0, getHeight()),
                toTexturePosition(getWidth(), getHeight()));
    }

    public void resize(float x, float y, float w, float h) {
        area.setXYWH(x, y, w, h);
        updateArea();
    }

    public void resize(float w, float h) {
        resize(area.getX1(), area.getY1(), w, h);
    }

    @Override
    public IQuad getRawQuad() {

        return rawQuad;
    }

    @Override
    public Vec2 toTexturePosition(float x, float y) {
        return texture.toTexturePosition(area.getX1() + x, area.getY1() + y);
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
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Bitmap toBitmap(MContext context) {
        int[] b = new int[getWidth() * getHeight()];
        IntBuffer buffer = IntBuffer.wrap(b);
        buffer.position(0);
        BufferedLayer layer = new BufferedLayer(context, getWidth(), getHeight(), false);
        GLCanvas2D canvas = new GLCanvas2D(layer);
        canvas.prepare();
        canvas.clearColor(Color4.Alpha);
        canvas.clearBuffer();
        canvas.save();
        canvas.drawTexture(this, RectF.xywh(0, 0, getWidth(), getHeight()));
        if (GLWrapped.GL_VERSION >= 2) {
            GLES20.glReadPixels(0, 0, getWidth(), getHeight(), GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        } else {
            GLES10.glReadPixels(0, 0, getWidth(), getHeight(), GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        }
        canvas.restore();
        canvas.unprepare();
        Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bmp.copyPixelsFromBuffer(buffer);
        buffer.clear();
        return bmp;
    }
}
