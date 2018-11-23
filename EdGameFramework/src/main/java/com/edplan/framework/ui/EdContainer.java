package com.edplan.framework.ui;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.drawable.sprite.FastTextureSprite;
import com.edplan.framework.ui.drawable.sprite.RoundedRectSprite;
import com.edplan.framework.ui.drawable.sprite.RoundedShadowSprite;


/**
 * 单独绘制一个FBO，然后再绘制到父View上，可以附加边缘效果
 */
public abstract class EdContainer extends EdAbstractViewGroup {
    private BufferedLayer layer;

    private BaseCanvas layerCanvas;

    private GLPaint postPaint;

    private LayerPoster layerPoster;

    private float pixelScale = 1;

    private boolean alwaysRefresh = false;

    private boolean needRefresh = true;

    private boolean useBuffer = false;

    public EdContainer(MContext c) {
        super(c);
        layer = new BufferedLayer(c);
        layerPoster = new DefaultLayerPoster(c);
        layerCanvas = new GLCanvas2D(layer);
        postPaint = new GLPaint();
    }

    public void setUseBuffer(boolean useBuffer) {
        this.useBuffer = useBuffer;
    }

    public boolean isUseBuffer() {
        return useBuffer;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setAlwaysRefresh(boolean alwaysRefresh) {
        this.alwaysRefresh = alwaysRefresh;
    }

    public boolean isAlwaysRefresh() {
        return alwaysRefresh;
    }

    public void setPixelScale(float pixelScale) {
        this.pixelScale = pixelScale;
    }

    public float getPixelScale() {
        return pixelScale;
    }

    public void setLayerPoster(LayerPoster layerPoster) {
        this.layerPoster = layerPoster;
    }

    public LayerPoster getLayerPoster() {
        return layerPoster;
    }

    public void setAlpha(float alpha) {
        postPaint.setFinalAlpha(alpha);
        invalidateDraw();
    }

    public float getAlpha() {
        return postPaint.getFinalAlpha();
    }

    public void setAccentColor(Color4 c) {
        postPaint.setMixColor(c);
        invalidateDraw();
    }

    public Color4 getAccentColor() {
        return postPaint.getMixColor();
    }

    public Vec2 getBufferSize() {
        return new Vec2(layer.getWidth(), layer.getHeight());
    }

    protected void updateLayerSize(BaseCanvas canvas) {
        layer.setWidth((int) (pixelScale * getWidth() / canvas.getPixelDensity()));
        layer.setHeight((int) (pixelScale * getHeight() / canvas.getPixelDensity()));
    }

    protected void updateCanvas(BaseCanvas canvas) {
        layerCanvas.reinitial();
        layerCanvas.scaleContent(canvas.getPixelDensity() / pixelScale);
        layerCanvas.clip(getWidth(), getHeight());
    }

    protected void drawContainer(BaseCanvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public void invalidateDraw() {
        super.invalidateDraw();
        needRefresh = true;
    }

    @Override
    protected void dispatchDraw(BaseCanvas canvas) {

		/*
		drawBackground(canvas);
		drawContainer(canvas);
		*/


        if (useBuffer) {
            if (needRefresh || alwaysRefresh) {
                needRefresh = false;
                updateLayerSize(canvas);
                updateCanvas(canvas);
                layerCanvas.prepare();
                layerCanvas.clearBuffer();
                drawBackground(layerCanvas);
                drawContainer(layerCanvas);
                layerCanvas.unprepare();
            }
            postLayer(canvas, layer, RectF.xywh(0, 0, getWidth(), getHeight()), postPaint);
        } else {
            canvas.unprepare();
            BaseCanvas clip = canvas.requestClipCanvas(0, 0, Math.round(getWidth()), Math.round(getHeight()));
            //clip.setCanvasAlpha(canvas.getCanvasAlpha());
            clip.prepare();
            //GLPaint paint = new GLPaint();
            //clip.drawTexture(GLTexture.ErrorTexture, RectF.ltrb(0, 0, clip.getWidth(), clip.getHeight()), Color4.ONE, 0.3f);
            drawBackground(clip);
            drawContainer(clip);
            clip.unprepare();
            canvas.prepare();
        }

    }

    protected void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, GLPaint paint) {
        if (layer.getTexture() == null) return;
        if (layerPoster != null) {
            layerPoster.postLayer(canvas, layer, area, paint);
            return;
        }
        canvas.getBlendSetting().save();
        canvas.getBlendSetting().setEnable(false);
        canvas.drawTexture(layer.getTexture(), area, paint);
        canvas.getBlendSetting().restore();
    }

    public RoundedLayerPoster setRounded(float radius) {
        RoundedLayerPoster p = new RoundedLayerPoster(getContext());
        p.setRoundedRadius(radius);
        layerPoster = p;
        return p;
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        dispatchDraw(canvas);
    }

    public interface LayerPoster {
        void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, GLPaint paint);
    }

    public static class DefaultLayerPoster implements LayerPoster {

        FastTextureSprite sprite;

        boolean enableBlending = true;

        public DefaultLayerPoster(MContext context) {
            sprite = new FastTextureSprite(context);
        }

        public void setEnableBlending(boolean enableBlending) {
            this.enableBlending = enableBlending;
        }

        public boolean isEnableBlending() {
            return enableBlending;
        }

        @Override
        public void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, GLPaint paint) {
            if(!enableBlending){
                canvas.getBlendSetting().save();
                canvas.getBlendSetting().setEnable(false);
            }
            sprite.setArea(area);
            sprite.setTexture(layer.getTexture());
            sprite.setAlpha(paint.getFinalAlpha());
            sprite.setAccentColor(paint.getMixColor());
            sprite.draw(canvas);
            if (!enableBlending) {
                canvas.getBlendSetting().restore();
            }
        }
    }

    public static class RoundedLayerPoster implements LayerPoster {
        private RoundedRectSprite sprite;

        private RoundedShadowSprite shadow;

        private MContext context;

        private Anchor anchor = Anchor.Center;

        private float rotation = 0;

        private float scaleX = 1, scaleY = 1;

        public RoundedLayerPoster(MContext c) {
            this.context = c;
            sprite = new RoundedRectSprite(c);
        }

        public void setAnchor(Anchor anchor) {
            this.anchor = anchor;
        }

        public Anchor getAnchor() {
            return anchor;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        public float getRotation() {
            return rotation;
        }

        public void setScale(float s) {
            setScaleX(s);
            setScaleY(s);
        }

        public void setScaleX(float scaleX) {
            this.scaleX = scaleX;
        }

        public float getScaleX() {
            return scaleX;
        }

        public void setScaleY(float scaleY) {
            this.scaleY = scaleY;
        }

        public float getScaleY() {
            return scaleY;
        }

        public void setRoundedRadius(float r) {
            sprite.setRoundedRadius(r);
            if (shadow != null) shadow.setRoundedRadius(r);
        }

        public float getRoundedRadius() {
            return sprite.getRoundedRadius();
        }

        public void setShadow(float width, Color4 start, Color4 end) {
            if (shadow == null) {
                shadow = new RoundedShadowSprite(context);
                shadow.setRoundedRadius(sprite.getRoundedRadius());
            }
            shadow.setShadowWidth(width);
            shadow.setShadowColor(start, end);
            shadow.setBlendType(BlendType.Additive);
        }

        @Override
        public void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, GLPaint paint) {

            Vec2 org = new Vec2(area.getLeft() + area.getWidth() * anchor.x(), area.getTop() + area.getHeight() * anchor.y());
            if (shadow != null) {
                shadow.setTexture(GLTexture.White);
                shadow.setAccentColor(paint.getMixColor());
                shadow.setAlpha(paint.getFinalAlpha());
                shadow.setArea(RectF.anchorOWH(anchor, org.x, org.y, (area.getWidth() + shadow.getShadowWidth() * 2) * scaleX, (area.getHeight() + shadow.getShadowWidth() * 2) * scaleY));
                shadow.setRect(area);
                shadow.draw(canvas);
            }

            sprite.setTexture(layer.getTexture());
            sprite.setAccentColor(paint.getMixColor());
            sprite.setAlpha(paint.getFinalAlpha());
            RectF a = RectF.anchorOWH(anchor, org.x, org.y, area.getWidth() * scaleX, area.getHeight() * scaleY);
            sprite.setArea(a);
            sprite.setRect(a);
            //	RectF.anchorOWH(anchor,org.x,org.y,area.getWidth()*scaleX,area.getHeight()*scaleY)
            //);
            canvas.save();
            canvas.rotate(org.x, org.y, rotation);
            sprite.draw(canvas);
            canvas.restore();
        }
    }
}
