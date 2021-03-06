package com.edlplan.framework.ui;

import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.GLCanvas2D;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.layer.BufferedLayer;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.ui.drawable.sprite.FastTextureSprite;
import com.edlplan.framework.ui.drawable.sprite.RoundedRectSprite;
import com.edlplan.framework.ui.drawable.sprite.RoundedShadowSprite;


/**
 * 单独绘制一个FBO，然后再绘制到父View上，可以附加边缘效果
 */
public abstract class EdBufferedContainer extends EdAbstractViewGroup {
    private BufferedLayer layer;

    private BaseCanvas layerCanvas;

    private float alpha = 1;

    private Color4 accentColor = Color4.White.copyNew();

    private Color4 clearColor = Color4.Alpha.copyNew();

    private LayerPoster layerPoster;

    private float pixelScale = 1;

    private boolean alwaysRefresh = false;

    private boolean needRefresh = true;

    public EdBufferedContainer(MContext c) {
        super(c);
        layer = new BufferedLayer(c);
        layerPoster = new DefaultLayerPoster(c);
        layerCanvas = new GLCanvas2D(layer);
    }

    public void setClearColor(Color4 clearColor) {
        this.clearColor.set(clearColor);
    }

    public void enableDepth() {
        if (!layer.getFrameBuffer().hasDepthAttachment()) {
            layer = new BufferedLayer(getContext(), true);
        }
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
        this.alpha = alpha;
        invalidateDraw();
    }

    public float getAlpha() {
        return alpha;
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
        layerCanvas.expendAxis(canvas.getPixelDensity() / pixelScale);
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
        if (needRefresh || alwaysRefresh) {
            needRefresh = false;
            updateLayerSize(canvas);
            updateCanvas(canvas);
            layerCanvas.prepare();
            layerCanvas.clearBuffer();
            layerCanvas.clearColor(clearColor);
            drawBackground(layerCanvas);
            drawContainer(layerCanvas);
            layerCanvas.unprepare();
        }
        postLayer(canvas, layer, RectF.xywh(0, 0, getWidth(), getHeight()), alpha, accentColor);
    }

    protected void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, float alpha, Color4 accentColor) {
        if (layer.getTexture() == null) return;
        if (layerPoster != null) {
            layerPoster.postLayer(canvas, layer, area, alpha, accentColor);
            return;
        } else {
            throw new RuntimeException("layerPoster is null");
        }
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
        void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, float alpha, Color4 accentColor);
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
        public void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, float alpha, Color4 accentColor) {
            if(!enableBlending){
                canvas.getBlendSetting().save();
                canvas.getBlendSetting().setEnable(false);
            }
            sprite.setArea(area);
            sprite.setTexture(layer.getTexture());
            sprite.setAlpha(alpha);
            sprite.setAccentColor(accentColor);
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
        public void postLayer(BaseCanvas canvas, BufferedLayer layer, RectF area, float alpha, Color4 accentColor) {

            Vec2 org = new Vec2(area.getLeft() + area.getWidth() * anchor.x(), area.getTop() + area.getHeight() * anchor.y());
            if (shadow != null) {
                shadow.setTexture(GLTexture.White);
                shadow.setAccentColor(accentColor);
                shadow.setAlpha(alpha);
                shadow.setArea(RectF.anchorOWH(anchor, org.x, org.y, (area.getWidth() + shadow.getShadowWidth() * 2) * scaleX, (area.getHeight() + shadow.getShadowWidth() * 2) * scaleY));
                shadow.setRect(area);
                shadow.draw(canvas);
            }

            sprite.setTexture(layer.getTexture());
            sprite.setAccentColor(accentColor);
            sprite.setAlpha(alpha);
            RectF a = RectF.anchorOWH(anchor, org.x, org.y, area.getWidth() * scaleX, area.getHeight() * scaleY);
            sprite.setArea(a);
            sprite.setRect(a);
            canvas.save();
            canvas.rotate(org.x, org.y, rotation);
            sprite.draw(canvas);
            canvas.restore();
        }
    }
}
