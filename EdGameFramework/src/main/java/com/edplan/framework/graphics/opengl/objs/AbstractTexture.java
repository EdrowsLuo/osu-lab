package com.edplan.framework.graphics.opengl.objs;

import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLES20;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Quad;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;

import java.nio.IntBuffer;

public abstract class AbstractTexture {
    public abstract int getTextureId();

    public abstract GLTexture getTexture();

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract Vec2 toTexturePosition(float x, float y);

    public abstract IQuad getRawQuad();

    public Vec2 toTexturePosition(Vec2 v) {
        return toTexturePosition(v.x, v.y);
    }

    //将一个width x height坐标的rect换成1x1坐标
    public RectF toTextureRect(RectF raw) {
        return toTextureRect(raw.getLeft(), raw.getTop(), raw.getRight(), raw.getBottom());
    }

    public RectF toTextureRect(float l, float t, float r, float b) {
        Vec2 lt = toTexturePosition(l, t);
        Vec2 rb = toTexturePosition(r, b);
        return RectF.ltrb(lt.x, lt.y, rb.x, rb.y);
    }

    public Quad toTextureQuad(IQuad q) {
        Quad r = new Quad();
        r.set(
                toTexturePosition(q.getTopLeft()),
                toTexturePosition(q.getTopRight()),
                toTexturePosition(q.getBottomLeft()),
                toTexturePosition(q.getBottomRight()));
        return r;
    }

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
        canvas.flush();
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
