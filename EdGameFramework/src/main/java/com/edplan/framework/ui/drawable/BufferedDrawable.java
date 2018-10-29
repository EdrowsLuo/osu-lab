package com.edplan.framework.ui.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.drawable.interfaces.IFadeable;
import com.edplan.framework.utils.MLog;

/**
 * 将Canvas上的一块用BufferedLayer包装，再绘制到canvas上去
 */
public abstract class BufferedDrawable extends EdDrawable implements IFadeable {
    private BufferedLayer bufferedLayer;

    private RectF area = new RectF();

    private GLPaint paint = new GLPaint();

    private boolean needUpdate = false;

    public BufferedDrawable(MContext c) {
        super(c);
    }

    /**
     * 必须在不再绘制这个view后调用，主动回收内存
     */
    public void recycle() {
        bufferedLayer.recycle();
    }

    public void setArea(RectF area) {
        this.area.set(area);
    }

    public RectF getArea() {
        return area;
    }

    @Override
    public void setAlpha(float a) {

        paint.setFinalAlpha(a);
    }

    @Override
    public float getAlpha() {

        return paint.getFinalAlpha();
    }

    public void postUpdate() {
        needUpdate = true;
    }

    private Vec2 calBufferSize(BaseCanvas canvas) {
        float x = area.getWidth() / canvas.getPixelDensity();
        float y = area.getHeight() / canvas.getPixelDensity();
        return new Vec2(x, y);
    }

    /**
     * 默认操作只同步pixelDensity
     */
    protected void transformCanvas(BaseCanvas nowCanvas, BaseCanvas parentCanvas) {
        nowCanvas.scaleContent(parentCanvas.getPixelDensity());
        //MLog.test.vOnce("dest","dest",parentCanvas.getPixelDensity()+"");
        nowCanvas.translate(-area.getLeft(), -area.getTop());
        //MLog.test.vOnce("n-canvas","n-canvas","parent:"+parentCanvas.getPixelDensity()+" now:"+nowCanvas.getPixelDensity());
    }

    /**
     * 如何将内容绘制到父Canvas上去
     */
    protected void postToParent(AbstractTexture texture, BaseCanvas canvas) {
        //canvas.drawTexture(GLTexture.White,new RectF(0,0,texture.getWidth(),texture.getHeight()),
        //				   RectF.ltrb(area.getX1(),area.getY1(),area.getX2(),area.getY2()),paint);
        canvas.drawTexture(texture, RectF.xywh(0, 0, area.getWidth() / canvas.getPixelDensity(), area.getHeight() / canvas.getPixelDensity()), RectF.ltrb(area.getX1(), area.getY1(), area.getX2(), area.getY2()), paint);
        MLog.test.vOnce("rect", "rect", area.toString());
    }

    /**
     * 如何绘制内容
     */
    protected abstract void drawContent(BaseCanvas canvas);

    @Override
    public void draw(BaseCanvas canvas) {

        Vec2 size = calBufferSize(canvas);
        if (bufferedLayer == null) {
            bufferedLayer = new BufferedLayer(getContext(), (int) size.x + 1, (int) size.y + 1, true);
            postUpdate();
        }
        int w = (int) size.x + 1;
        int h = (int) size.y + 1;
        if (bufferedLayer.getWidth() != w || bufferedLayer.getHeight() != h) {
            bufferedLayer.setWidth(w);
            bufferedLayer.setHeight(h);
            postUpdate();
        }
        if (needUpdate) {
            GLCanvas2D ncanvas = new GLCanvas2D(bufferedLayer);
            ncanvas.prepare();
            transformCanvas(ncanvas, canvas);
            canvas.drawColor(Color4.White);
            drawContent(ncanvas);
            ncanvas.unprepare();
            needUpdate = false;
        }
        postToParent(bufferedLayer.getTexture(), canvas);
    }
}
