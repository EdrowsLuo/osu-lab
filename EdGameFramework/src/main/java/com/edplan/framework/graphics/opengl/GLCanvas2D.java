package com.edplan.framework.graphics.opengl;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.batch.BaseColorBatch;
import com.edplan.framework.graphics.opengl.batch.RectVertexBatch;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.objs.Vertex3D;
import com.edplan.framework.graphics.opengl.objs.vertex.RectVertex;
import com.edplan.framework.graphics.opengl.shader.advance.RectTextureShader;
import com.edplan.framework.graphics.opengl.shader.advance.RoundedRectTextureShader;
import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.math.Vec4;
import com.edplan.framework.utils.AbstractSRable;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edplan.framework.utils.MLog;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.interfaces.ITexture3DBatch;
import com.edplan.framework.media.video.tbv.element.DataDrawBaseTexture;

public class GLCanvas2D extends BaseCanvas // extends AbstractSRable<CanvasData>
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
        d.setShaders(ShaderManager.getNewDefault());
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
    }

    @Override
    protected void onUnprepare() {
        checkPrepared(
                "you can't call unprepare when GLCanvas isn't prepared",
                true);
        getLayer().unbind();
    }

    @Override
    public void delete() {
        checkPrepared("you delete a prepared canvas!", false);
        recycle();
    }

    @Override
    public void drawColor(Color4 color) {
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

    @Override
    protected BaseCanvas clipCanvas(int x, int y, int width, int height) {

        return super.clipCanvas(x, y, width, height);
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
