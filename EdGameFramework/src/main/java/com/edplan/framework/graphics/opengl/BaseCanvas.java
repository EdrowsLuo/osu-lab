package com.edplan.framework.graphics.opengl;

import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.BaseColorBatch;
import com.edplan.framework.graphics.opengl.batch.RectVertexBatch;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.batch.interfaces.ITexture3DBatch;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.batch.v2.object.AnyQuadTextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.objs.Vertex3D;
import com.edplan.framework.graphics.opengl.objs.vertex.RectVertex;
import com.edplan.framework.graphics.opengl.shader.advance.LegacyTexture3DShader;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.utils.AbstractSRable;


/**
 * 事实证明，既然自己是辣鸡就别写Canvas这种重量级东西了x
 */
public abstract class BaseCanvas extends AbstractSRable<CanvasData> {

    private ITexture3DBatch<TextureVertex3D> tmpBatch;

    /**
     * 对于开启了post的应该手动post
     */
    private boolean enablePost = false;

    private int maxBatchSize = 5000;

    private int drawCalls = 0;

    public BaseCanvas() {
        initialBatch();
    }

    public BaseCanvas(boolean initial) {
        if (initial) initialBatch();
    }

    protected void initialBatch() {
        tmpBatch = createTexture3DBatch();
    }

    @Deprecated
    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    @Deprecated
    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    @Deprecated
    protected ITexture3DBatch<TextureVertex3D> createTexture3DBatch() {
        return new Texture3DBatch<TextureVertex3D>();
        //return new DataDrawBaseTexture(6);
    }

    @Deprecated
    protected RectVertexBatch<RectVertex> createRectVertexBatch() {
        return new RectVertexBatch<>();
    }

    @Deprecated
    protected BaseColorBatch<Vertex3D> createColorBatch() {
        return new BaseColorBatch<>();
    }

    public int getDrawCalls() {
        return drawCalls;
    }

    @Deprecated
    public boolean isEnablePost() {
        return enablePost;
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
            BatchEngine.getShaderGlobals().alpha = a;
            getData().setCanvasAlpha(a);
        }
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
        BatchEngine.setGlobalCamera(now.getCamera());
    }

    public void delete() {
        recycle();
    }


    @Deprecated
    private void injectData(BaseBatch batch, AbstractTexture texture, float alpha, Color4 mixColor, LegacyTexture3DShader shader) {
        shader.useThis();
        shader.loadAccentColor(mixColor);
        shader.loadAlpha(alpha * getCanvasAlpha());
        shader.loadMatrix(getCamera());
        shader.loadTexture(texture.getTexture());
        shader.loadBatch(batch);
    }

    @Deprecated
    public void drawTexture3DBatch(BaseBatch batch, AbstractTexture texture, float alpha, Color4 mixColor) {
        checkCanDraw();
        injectData(batch, texture.getTexture(), alpha, mixColor, getData().getTexture3DShader());
        getData().getTexture3DShader().applyToGL(GLWrapped.GL_TRIANGLES, 0, batch.getVertexCount());
    }

    public void drawTexture(AbstractTexture texture, IQuad dst) {
        drawTexture(texture, dst, 1);
    }


    public void drawTexture(AbstractTexture texture, IQuad dst, float alpha) {
        //TODO : 重写drawTexture(AbstractTexture texture, IQuad dst)
        AnyQuadTextureQuad anyQuadTextureQuad = new AnyQuadTextureQuad();
        anyQuadTextureQuad.texture = texture;
        anyQuadTextureQuad.textureQuad = texture.getRawQuad();
        anyQuadTextureQuad.positionQuad = dst;
        anyQuadTextureQuad.alpha.value = alpha;
        TextureQuadBatch.getDefaultBatch().add(anyQuadTextureQuad);
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

    @Override
    public void recycle() {
        tmpBatch.clear();
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();
    }
}
