package com.edplan.framework.graphics.opengl;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Mat4;

public class GLCanvas2D extends BaseCanvas
{
    private BufferedLayer layer;

    public GLCanvas2D(BufferedLayer layer) {
        this.layer = layer;
        initial();
    }

    public GLCanvas2D(GLTexture texture, MContext context) {
        this(new BufferedLayer(context, texture, true));
    }

    @Override
    public void onSave(CanvasData t) {

    }

    @Override
    public void onRestore(CanvasData now, CanvasData pre) {
        pre.recycle();
    }

    @Override
    public CanvasData getDefData() {
        CanvasData d = new CanvasData();
        d.setCurrentProjMatrix(layer.createProjMatrix());
        d.setCurrentMaskMatrix(Mat4.createIdentity());
        d.setHeight(getLayer().getHeight());
        d.setWidth(getLayer().getWidth());
        return d;
    }

    @Override
    public BlendSetting getBlendSetting() {
        return GLWrapped.blend;
    }

    @Override
    public int getDefHeight() {
        return layer.getHeight();
    }

    @Override
    public int getDefWidth() {
        return layer.getWidth();
    }

    @Override
    public void checkCanDraw() {
        checkPrepared(
                "canvas hasn't prepared for draw",
                true);
    }

    public BufferedLayer getLayer() {
        return layer;
    }

    @Override
    public boolean isPrepared() {
        return getLayer().isBind();
    }

    @Override
    protected void onPrepare() {
        checkPrepared(
                "you can't call prepare when GLCanvas is prepared",
                false);
        getLayer().bind();
        BatchEngine.getShaderGlobals().alpha = getCanvasAlpha();
    }

    @Override
    protected void onUnprepare() {
        checkPrepared(
                "you can't call unprepare when GLCanvas isn't prepared",
                true);
        getLayer().unbind();
    }

    @Override
    public void clearColor(Color4 color) {
        GLWrapped.setClearColor(color.r, color.g, color.b, color.a);
        GLWrapped.clearColorBuffer();
    }

    public void clearDepthBuffer() {
        GLWrapped.clearDepthBuffer();
    }

    @Override
    public void clearBuffer() {
        GLWrapped.clearDepthAndColorBuffer();
    }

    public MContext getContext() {
        return getLayer().getContext();
    }

    @Override
    public boolean supportClip() {
        return true;
    }

    /**
     * 裁剪Canvas
     */
    @Override
    protected BaseCanvas clipCanvas(float x, float y, float width, float height) {
        int ix = Math.round((getData().getTheOrigin().x + x) / getData().getPixelDensity());
        int iy = Math.round((getData().getTheOrigin().y + y) / getData().getPixelDensity());
        int ixw = Math.round((getData().getTheOrigin().x + x + width) / getData().getPixelDensity());
        int iyh = Math.round((getData().getTheOrigin().y + y + height) / getData().getPixelDensity());
        GLCanvas2D canvas2D = new GLCanvas2D(new BufferedLayer(layer, ix, iy, ixw - ix, iyh - iy));
        canvas2D.setCanvasAlpha(getCanvasAlpha());
        if (getData().getPixelDensity() != 1) {
            canvas2D.expendAxis(getData().getPixelDensity());
        }
        return canvas2D;
    }

    @Override
    public void recycle() {
        super.recycle();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
