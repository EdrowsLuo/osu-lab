package com.edplan.framework.graphics.opengl;

import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.batch.v2.object.AnyQuadTextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.ColorTriangle;
import com.edplan.framework.graphics.opengl.batch.v2.object.PackedColorTriangles;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edplan.framework.graphics.shape.IPath;
import com.edplan.framework.graphics.shape.Path;
import com.edplan.framework.graphics.shape.PathBuilder;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.polygon.PolygonMath;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.utils.AbstractSRable;
import com.edplan.framework.utils.FloatRef;


/**
 * 事实证明，既然自己是辣鸡就别写Canvas这种重量级东西了x
 */
public abstract class BaseCanvas extends AbstractSRable<CanvasData> {

    public BaseCanvas() {

    }

    public BaseCanvas translate(float tx, float ty) {
        getData().translate(tx, ty);
        BatchEngine.setGlobalCamera(getData().getCamera());
        return this;
    }

    public BaseCanvas rotate(float r) {
        getData().rotate(r);
        BatchEngine.setGlobalCamera(getData().getCamera());
        return this;
    }

    public BaseCanvas rotate(float ox, float oy, float r) {
        getData().rotate(ox, oy, r);
        BatchEngine.setGlobalCamera(getData().getCamera());
        return this;
    }

    /**
     * 对画布对应区域缩放<br>
     * 
     */
    public BaseCanvas scale(float x, float y) {
        getData().scale(x, y);
        BatchEngine.setGlobalCamera(getData().getCamera());
        return this;
    }

    /**
     * 拉伸轴，对应的实际像素数不会改变
     * 比如当前的Canvas是100x100大小对应100x100的像素，<br>
     * expendAxis(2)后就变成了200x200的大小对应100x100的像素
     */
    public BaseCanvas expendAxis(float s) {
        getData().expendAxis(s);
        BatchEngine.setGlobalCamera(getData().getCamera());
        return this;
    }

    /**
     * 限制Canvas范围，但是并没有限制外部像素绘制
     */
    public BaseCanvas clip(float w, float h) {
        getData().clip(w, h);
        BatchEngine.setGlobalCamera(getData().getCamera());
        return this;
    }

    public float getPixelDensity() {
        return getData().getPixelDensity();
    }

    public float getWidth() {
        return getData().getWidth();
    }

    public float getHeight() {
        return getData().getHeight();
    }

    public Camera getCamera() {
        return getData().getCamera();
    }

    public float getCanvasAlpha() {
        return getData().getCanvasAlpha();
    }

    public void setCanvasAlpha(float a) {
        if (Math.abs(a - getData().getCanvasAlpha()) > 0.0001) {
            BatchEngine.flush();
        }
        getData().setCanvasAlpha(a);
        BatchEngine.getShaderGlobals().alpha = a;
    }

    /**
     * @param p:当前应该是的状态
     */
    public void checkPrepared(String msg, boolean p) {
        if (p != isPrepared()) {
            throw new GLException("prepare err [n,c]=[" + p + "," + isPrepared() + "] msg: " + msg);
        }
    }

    public abstract boolean isPrepared();

    public final void prepare() {
        GLWrapped.prepareCanvas(this);
    }

    protected abstract void onPrepare();

    public void unprepare() {
        GLWrapped.unprepareCanvas(this);
    }

    protected abstract void onUnprepare();

    @Override
    public void onSave(CanvasData t) {

    }

    @Override
    public void onRestore(CanvasData now, CanvasData pre) {
        pre.recycle();
        BatchEngine.getShaderGlobals().alpha = now.getCanvasAlpha();
        BatchEngine.setGlobalCamera(now.getCamera());
    }

    public void drawTexture(AbstractTexture texture, IQuad dst) {
        drawTexture(texture, dst, 1);
    }

    public void drawTexture(AbstractTexture texture, IQuad dst, float alpha) {
        AnyQuadTextureQuad anyQuadTextureQuad = new AnyQuadTextureQuad();
        anyQuadTextureQuad.texture = texture;
        anyQuadTextureQuad.textureQuad = texture.getRawQuad();
        anyQuadTextureQuad.positionQuad = dst;
        anyQuadTextureQuad.alpha.value = alpha;
        TextureQuadBatch.getDefaultBatch().add(anyQuadTextureQuad);
    }

    public void drawLine(float x1, float y1, float x2, float y2, float width, Color4 color, float alpha) {

    }

    private static final int CIRCLE_SPLIT_RATE = 48, CIRCLE_SPLIT_RATE_HALF = 24, CIRCLE_SPLIT_RATE_HH = 12;
    private static final double CIRCLE_SPLIT_ANGLE = Math.PI / CIRCLE_SPLIT_RATE_HALF;
    public void drawCircle(float ox, float oy, float radius, Color4 color, float alpha) {
        Vec2[] polygon = new Vec2[CIRCLE_SPLIT_RATE];
        float ox2 = ox * 2;
        float oy2 = oy * 2;
        for (int i = 0; i< CIRCLE_SPLIT_RATE_HALF;i++) {
            double theta = i * CIRCLE_SPLIT_ANGLE;
            Vec2 v = new Vec2(
                    (float) Math.cos(theta) * radius + ox,
                    (float) Math.sin(theta) * radius + oy
            );
            polygon[i] = v;
            polygon[CIRCLE_SPLIT_RATE_HALF + i] = new Vec2(-v.x + ox2, -v.y + oy2);
        }
        Vec2[] spl = new Vec2[3 * (CIRCLE_SPLIT_RATE - 2)];
        PolygonMath.divideConvexPolygon(polygon, spl);
        PackedColorTriangles packedColorTriangles = new PackedColorTriangles();
        makeUpTriangles(spl, packedColorTriangles, color, alpha);
        packedColorTriangles.render(ColorShader.DEFAULT.get(),BatchEngine.getShaderGlobals());
    }

    public void drawConvexPolygon(Vec2[] polygon, int offset, int length, Color4 color, float alpha) {
        Vec2[] spl = new Vec2[3 * (length - 2)];
        PolygonMath.divideConvexPolygon(polygon, offset, length, spl, 0);
        PackedColorTriangles packedColorTriangles = new PackedColorTriangles();
        makeUpTriangles(spl, packedColorTriangles, color, alpha);
        packedColorTriangles.render(ColorShader.DEFAULT.get(),BatchEngine.getShaderGlobals());
    }

    public void drawConvexPolygon(Vec2[] polygon, Color4 color, float alpha) {
        drawConvexPolygon(polygon, 0, polygon.length, color, alpha);
    }

    public void drawRect(IQuad quad, Color4 color, float alpha) {
        drawConvexPolygon(
                new Vec2[]{
                        quad.getTopLeft(),
                        quad.getTopRight(),
                        quad.getBottomRight(),
                        quad.getBottomLeft()
                },
                color,
                alpha);
    }

    public void drawPath(IPath path, Color4 color, float alpha) {
        drawConvexPolygon(path.buffer(), path.offset(), path.size(), color, alpha);
    }

    public void drawRoundedRect(RectF rectF, float radius, Color4 color, float alpha) {
        Path path = new Path(CIRCLE_SPLIT_RATE);
        radius = Math.min(radius, Math.min(rectF.getWidth() / 2, rectF.getHeight() / 2));

        PathBuilder builder = new PathBuilder(path);
        builder.moveTo(rectF.getRight() - radius, rectF.getTop());
        builder.circleMove(new Vec2(rectF.getRight() - radius, rectF.getTop() + radius), FMath.PiHalf);
        builder.moveTo(rectF.getRight(), rectF.getBottom() - radius);
        builder.circleMove(new Vec2(rectF.getRight() - radius, rectF.getBottom() - radius), FMath.PiHalf);
        builder.moveTo(rectF.getLeft() + radius, rectF.getBottom());
        builder.circleMove(new Vec2(rectF.getLeft() + radius, rectF.getBottom() - radius), FMath.PiHalf);
        builder.moveTo(rectF.getLeft(), rectF.getTop() + radius);
        builder.circleMove(new Vec2(rectF.getLeft() + radius, rectF.getTop() + radius), FMath.PiHalf);

        drawPath(path, color, alpha);
    }

    private void makeUpTriangles(Vec2[] t, PackedColorTriangles colorTriangles, Color4 color4, float a) {
        for (int i = 0; i < t.length; i += 3) {
            colorTriangles.add(
                    new ColorTriangle(
                            t[i],
                            t[i+1],
                            t[i+2],
                            a,
                            color4
                    )
            );
        }
    }
















    /**
     * @return 返回是否支持裁剪画板的一部分
     */
    public boolean supportClip() {
        return false;
    }

    /**
     * 返回一个被裁剪的画板，
     * @param x 裁剪区域起始x
     * @param y 裁剪区域起始y
     * @param width 裁剪区域宽度
     * @param height 裁剪区域高度
     * @return 返回一个新画板，画板的新原点为裁剪起点（会产生新对象）
     */
    protected BaseCanvas clipCanvas(float x, float y, float width, float height) {
        return null;
    }


    public final BaseCanvas requestClipCanvas(float x, float y, float width, float height) {
        checkPrepared("you can only clip canvas when it is not working", false);
        if (supportClip()) {
            return clipCanvas(x, y, width, height);
        } else {
            return null;
        }
    }


    public abstract int getDefWidth();

    public abstract int getDefHeight();

    public abstract BlendSetting getBlendSetting();

    protected abstract void checkCanDraw();

    public abstract CanvasData getDefData();

    public abstract void clearBuffer();

    public abstract void clearColor(Color4 c);

    public void flush() {
        BatchEngine.flush();
    }

    @Override
    public void recycle() {

    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();
    }
}
