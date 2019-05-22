package com.edlplan.framework.graphics.opengl;

import com.edlplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edlplan.framework.graphics.opengl.batch.v2.object.AnyQuadTextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.PackedColorTriangles;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.graphics.shape.IPath;
import com.edlplan.framework.graphics.shape.Path;
import com.edlplan.framework.graphics.shape.PathBuilder;
import com.edlplan.framework.math.polygon.PolygonMath;
import com.edlplan.framework.graphics.opengl.batch.v2.object.ColorTriangle;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edlplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.IQuad;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.AbstractSRable;


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
        flush();
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

    public TextureQuad createLineQuad(float x1, float y1, float x2, float y2, float width, Color4 color, float alpha) {
        TextureQuad quad = new TextureQuad();
        quad.setTextureAndSize(GLTexture.White);
        quad.size.set((float) Math.hypot(x1 - x2, y1 - y2), width);
        quad.alpha.value = alpha;
        quad.accentColor = color;
        quad.position.set((x1 + x2) / 2, (y1 + y2) / 2);
        quad.enableRotation().rotation.value = (float) Math.atan2(y1 - y2, x1 - x2);
        return quad;
    }

    public TextureQuad createLineQuad(Vec2 v1, Vec2 v2, float width, Color4 color, float alpha) {
        return createLineQuad(v1.x, v1.y, v2.x, v2.y, width, color, alpha);
    }

    public void drawLine(Vec2 v1, Vec2 v2, float width, Color4 color, float alpha) {
        drawLine(v1.x, v1.y, v2.x, v2.y, width, color, alpha);
    }

    public void drawLine(float x1, float y1, float x2, float y2, float width, Color4 color, float alpha) {
        TextureQuadBatch.getDefaultBatch().add(createLineQuad(x1, y1, x2, y2, width, color, alpha));
    }

    private static final int CIRCLE_SPLIT_RATE = 48, CIRCLE_SPLIT_RATE_HALF = 24, CIRCLE_SPLIT_RATE_HH = 12;
    private static final double CIRCLE_SPLIT_ANGLE = Math.PI / CIRCLE_SPLIT_RATE_HALF;
    private static final float SIN_ANGLE = (float) Math.sin(CIRCLE_SPLIT_ANGLE), COS_ANGLE = (float) Math.cos(CIRCLE_SPLIT_ANGLE);
    public void drawCircle(float ox, float oy, float radius, Color4 color, float alpha) {
        Path path = new Path(CIRCLE_SPLIT_RATE);
        PathBuilder builder = new PathBuilder(path);
        builder.moveTo(ox + radius, oy);
        builder.circleRotate(new Vec2(ox, oy), FMath.Pi2);
        drawPath(path, color, alpha);
    }

    public void drawRing(float ox, float oy, float radius, float innerRadius, Color4 color, float alpha) {
        Vec2[] spl = new Vec2[6 * CIRCLE_SPLIT_RATE];
        Vec2 p1 = new Vec2(ox + radius, oy), p2 = p1.copy().rotate(ox, oy, SIN_ANGLE, COS_ANGLE);
        Vec2 p1i = new Vec2(ox + innerRadius, oy), p2i = p1i.copy().rotate(ox, oy, SIN_ANGLE, COS_ANGLE);
        for (int i = 0; i < spl.length; i += 6) {
            spl[i] = p1;
            spl[i + 1] = spl[i + 3] = p1i;
            spl[i + 2] = spl[i + 5] = p2;
            spl[i + 4] = p2i;
            p1 = p2;
            p1i = p2i;
            p2 = p1.copy().rotate(ox, oy, SIN_ANGLE, COS_ANGLE);
            p2i = p1i.copy().rotate(ox, oy, SIN_ANGLE, COS_ANGLE);
        }
        PackedColorTriangles packedColorTriangles = new PackedColorTriangles();
        makeUpTriangles(spl, packedColorTriangles, color, alpha);
        packedColorTriangles.render(ColorShader.DEFAULT.get(),BatchEngine.getShaderGlobals());
    }

    public void drawConvexPolygon(Vec2[] polygon, int offset, int length, Color4 color, float alpha, ColorShader shader) {
        Vec2[] spl = new Vec2[3 * (length - 2)];
        PolygonMath.divideConvexPolygon(polygon, offset, length, spl, 0);
        PackedColorTriangles packedColorTriangles = new PackedColorTriangles();
        makeUpTriangles(spl, packedColorTriangles, color, alpha);
        packedColorTriangles.render(shader, BatchEngine.getShaderGlobals());
    }

    public void drawConvexPolygon(Vec2[] polygon, Color4 color, float alpha, ColorShader shader) {
        drawConvexPolygon(polygon, 0, polygon.length, color, alpha, shader);
    }

    public void drawConvexPolygon(Vec2[] polygon, Color4 color, float alpha) {
        drawConvexPolygon(polygon, color, alpha, ColorShader.DEFAULT.get());
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
        drawPath(path, color, alpha, ColorShader.DEFAULT.get());
    }

    public void drawPath(IPath path, Color4 color, float alpha, ColorShader shader) {
        drawConvexPolygon(path.buffer(), path.offset(), path.size(), color, alpha, shader);
    }

    public void drawRoundedRect(RectF rectF, float radius, Color4 color, float alpha) {
        Path path = new Path(CIRCLE_SPLIT_RATE);
        radius = Math.min(radius, Math.min(rectF.getWidth() / 2, rectF.getHeight() / 2));

        PathBuilder builder = new PathBuilder(path);
        builder.moveTo(rectF.getRight() - radius, rectF.getTop());
        builder.circleRotate(new Vec2(rectF.getRight() - radius, rectF.getTop() + radius), FMath.PiHalf);
        builder.moveTo(rectF.getRight(), rectF.getBottom() - radius);
        builder.circleRotate(new Vec2(rectF.getRight() - radius, rectF.getBottom() - radius), FMath.PiHalf);
        builder.moveTo(rectF.getLeft() + radius, rectF.getBottom());
        builder.circleRotate(new Vec2(rectF.getLeft() + radius, rectF.getBottom() - radius), FMath.PiHalf);
        builder.moveTo(rectF.getLeft(), rectF.getTop() + radius);
        builder.circleRotate(new Vec2(rectF.getLeft() + radius, rectF.getTop() + radius), FMath.PiHalf);

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
