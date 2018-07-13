package com.edplan.nso.storyboard.elements.drawable;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.GL10Canvas2D;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.interfaces.FloatInvokeSetter;
import com.edplan.framework.interfaces.InvokeSetter;
import com.edplan.framework.math.Quad;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.animation.QueryAnimation;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.nso.storyboard.elements.StoryboardSprite;
import com.edplan.nso.storyboard.elements.drawable.BaseDrawableSprite;

import java.util.ArrayList;
import java.util.List;

import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.framework.graphics.opengl.fast.FastRenderer;
import com.edplan.framework.graphics.opengl.fast.FastQuad;

import java.util.Arrays;

import com.edplan.framework.math.FMath;
import com.edplan.nso.storyboard.renderer.OsbRenderer;

public class BaseDrawableSprite extends ADrawableStoryboardElement {
    protected AbstractTexture texture;

    private Anchor anchor = Anchor.Center;

    private Vec2 currentPosition = new Vec2();
    public static FloatInvokeSetter<BaseDrawableSprite> XRaw = new FloatInvokeSetter<BaseDrawableSprite>() {
        @Override
        public void invoke(BaseDrawableSprite target, float value) {

            target.currentPosition.x = value;
            //target.invalidateQuad();
        }
    };
    public static FloatInvokeSetter<BaseDrawableSprite> YRaw = new FloatInvokeSetter<BaseDrawableSprite>() {
        @Override
        public void invoke(BaseDrawableSprite target, float value) {

            target.currentPosition.y = value;
            //target.invalidateQuad();
        }
    };


    private float rotation = 0;
    public static FloatInvokeSetter<BaseDrawableSprite> RotationRaw = new FloatInvokeSetter<BaseDrawableSprite>() {
        @Override
        public void invoke(BaseDrawableSprite target, float value) {

            target.rotation = value;
            //target.invalidateQuad();
        }
    };


    private Vec2 scale = new Vec2(1, 1);
    public static FloatInvokeSetter<BaseDrawableSprite> ScaleXRaw = new FloatInvokeSetter<BaseDrawableSprite>() {
        @Override
        public void invoke(BaseDrawableSprite target, float value) {

            target.scale.x = value;
            //target.invalidateQuad();
        }
    };
    public static FloatInvokeSetter<BaseDrawableSprite> ScaleYRaw = new FloatInvokeSetter<BaseDrawableSprite>() {
        @Override
        public void invoke(BaseDrawableSprite target, float value) {

            target.scale.y = value;
            //target.invalidateQuad();
        }
    };


    private boolean flipH = false;
    public static InvokeSetter<BaseDrawableSprite, Boolean> FlipH = new InvokeSetter<BaseDrawableSprite, Boolean>() {
        @Override
        public void invoke(BaseDrawableSprite target, Boolean value) {

            target.flipH = value;
            //target.invalidateQuad();
        }
    };

    private boolean flipV = false;
    public static InvokeSetter<BaseDrawableSprite, Boolean> FlipV = new InvokeSetter<BaseDrawableSprite, Boolean>() {
        @Override
        public void invoke(BaseDrawableSprite target, Boolean value) {

            target.flipV = value;
            //target.invalidateQuad();
        }
    };

    private float alpha = 1;
    public static FloatInvokeSetter<BaseDrawableSprite> AlphaRaw = new FloatInvokeSetter<BaseDrawableSprite>() {
        @Override
        public void invoke(BaseDrawableSprite target, float value) {

            target.alpha = value;
        }
    };


    private Color4 varyingColor = Color4.ONE.copyNew();
    public static InvokeSetter<BaseDrawableSprite, Color4> Color = new InvokeSetter<BaseDrawableSprite, Color4>() {
        @Override
        public void invoke(BaseDrawableSprite target, Color4 value) {

            target.varyingColor.set(value);
        }
    };

    private BlendType blendType = BlendType.Normal;
    public static InvokeSetter<BaseDrawableSprite, BlendType> Blend = new InvokeSetter<BaseDrawableSprite, BlendType>() {
        @Override
        public void invoke(BaseDrawableSprite target, BlendType value) {

            target.blendType = value;
        }
    };

    private boolean needReCreateQuad = true;

    private double startTime;

    private double endTime;

    private String path;

    private BasePreciseAnimation[] animations = new BasePreciseAnimation[9];

    private int idx;

    private BasePreciseAnimation alphaAnimation;

    private RectF rawRect = new RectF();

    private Quad quad = new Quad();

    public BaseDrawableSprite(PlayingStoryboard storyboard, StoryboardSprite sprite) {
        super(storyboard);
        path = sprite.getPath();
        startTime = sprite.getStartTime();
        endTime = sprite.getEndTime();
        //MLog.test.vOnce("sprite-data",
        //System.out.println("sprite-data: "+startTime+","+endTime);
        anchor = sprite.getAnchor();
        currentPosition.set(sprite.getInitialPosition());
    }

    public void invalidateQuad() {
        needReCreateQuad = true;
    }

    public BasePreciseAnimation[] getAnimations() {
        return animations;
    }

    public void addAnimation(BasePreciseAnimation anim, InvokeSetter setter) {
        //if(setter!=Alpha){
        animations[idx++] = anim;
        //}else{
        //	alphaAnimation=anim;
        //}
    }

    public void addAnimation(BasePreciseAnimation anim, FloatInvokeSetter setter) {
        if (setter != AlphaRaw) {
            animations[idx++] = anim;
        } else {
            alphaAnimation = anim;
        }
    }


    @Override
    public double getStartTime() {

        return startTime;
    }

    @Override
    public double getEndTime() {

        return endTime;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setBlendType(BlendType blendType) {
        this.blendType = blendType;
    }

    public BlendType getBlendType() {
        return blendType;
    }

    public void setVaryingColor(Color4 varyingColor) {
        this.varyingColor = varyingColor;
    }

    public Color4 getVaryingColor() {
        return varyingColor;
    }

    public void setTexture(AbstractTexture texture) {
        this.texture = texture;
    }

    public AbstractTexture getTexture() {
        return texture;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setCurrentPosition(Vec2 currentPosition) {
        this.currentPosition.set(currentPosition);
    }

    public Vec2 getCurrentPosition() {
        return currentPosition;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setScale(Vec2 scale) {
        this.scale.set(scale);
    }

    public Vec2 getScale() {
        return scale;
    }

    protected void refreshAnimation(BasePreciseAnimation anim, double time) {
        if (anim != null) anim.postProgressTime(time - anim.currentTime());
    }

    boolean hasAdd = false;

    @Override
    public void onAdd() {

        hasAdd = true;
        if (alphaAnimation != null) alphaAnimation.onStart();
        for (BasePreciseAnimation anim : animations) {
            if (anim == null) break;
            anim.onStart();
        }
    }

    @Override
    public boolean hasAdd() {

        return hasAdd;
    }

    @Override
    public void onRemove() {

        for (BasePreciseAnimation anim : animations) {
            if (anim == null) break;
            anim.onFinish();
            anim.onEnd();
            anim.dispos();
        }
        Arrays.fill(animations, null);
        if (alphaAnimation != null) {
            alphaAnimation.onFinish();
            alphaAnimation.onEnd();
            alphaAnimation.dispos();
        }
    }

    @Override
    public void prepareForDraw() {

        double time = getTimeline().frameTime();
        refreshAnimation(alphaAnimation, time);
        if (alpha < 0.002) return;
        for (BasePreciseAnimation anim : animations) {
            if (anim == null) break;
            refreshAnimation(anim, time);
        }
    }

    final Vec2 bufferedAnchor = new Vec2();

    public void updateQuad() {
        rawRect
                .thisAnchorOWH(
                        anchor,
                        currentPosition.x,
                        currentPosition.y,
                        texture.getWidth(),
                        texture.getHeight())
                .scale(anchor, scale.x, scale.y)
                .toQuad(quad);
        if (Math.abs(rotation) > 0.01) {
            rawRect.getPoint(anchor.x(), anchor.y(), bufferedAnchor);
            quad.rotate(bufferedAnchor.x, bufferedAnchor.y, rotation);
        }
        //quad.rotate(quad.getPointByAnchor(anchor),rotation);
        quad.flip(flipH, flipV);
    }

    FastQuad fastQuad;

    @Override
    public void drawFastRenderer(FastRenderer renderer) {

        if (alpha < 0.002) return;
        updateQuad();
		/*
		rawRect
			.thisAnchorOWH(
			anchor,
			currentPosition.x,
			currentPosition.y,
			texture.getWidth(),
			texture.getHeight())
			.scale(anchor,scale.x,scale.y);*/
        if (fastQuad == null) fastQuad = new FastQuad();
        renderer.setBlendType(blendType);
        renderer.setCurrentTexture(texture);
        fastQuad.load(renderer);
		/*
		fastQuad.RawQuad.set(rawRect);
		fastQuad.RawQuad.rotare(anchor,rotation);
		fastQuad.RawQuad.flip(flipH,flipV);
		*/
        fastQuad.setColor(varyingColor.r, varyingColor.g, varyingColor.b, varyingColor.a * alpha);
        fastQuad.setQuad(quad);
        fastQuad.setTextureCoord(texture.getRawQuad());
        fastQuad.addToRenderer(renderer);
    }

    @Override
    public void drawOsbRenderer(OsbRenderer renderer) {

    }

    @Override
    public void draw(BaseCanvas canvas) {

        if (alpha < 0.002) return;
        if (needReCreateQuad) {
            updateQuad();
            needReCreateQuad = false;
        }
        //默认只按次流程绘制且只绘制StoryboardSprite，这里省去save/restore节省时间
        canvas.getBlendSetting().setBlendType(blendType);
        canvas.drawTexture(texture, quad, varyingColor, alpha);
		/*
		if(flipV){
			canvas.drawTexture(texture,quad,varyingColor,alpha*0.3f);
		}else{
			canvas.drawTexture(texture,quad,varyingColor,alpha);
		}
		*/
    }

    @Override
    public void drawGL10(GL10Canvas2D canvas) {

        double time = getTimeline().frameTime();
        refreshAnimation(alphaAnimation, time);
        if (alpha < 0.002) return;
        for (BasePreciseAnimation anim : animations) {
            refreshAnimation(anim, time);
        }
        Quad quad =
                RectF.anchorOWH(
                        anchor,
                        currentPosition.x,
                        currentPosition.y,
                        texture.getWidth(),
                        texture.getHeight()
                ).scale(anchor, scale.x, scale.y)
                        .toQuad();
        //quad.rotate(anchor,rotation);
        quad.flip(flipH, flipV);
        //默认只按次流程绘制且只绘制StoryboardSprite，这里省去save/restore节省时间
        GLWrapped.blend.setBlendType(blendType);
        canvas.drawTexture(texture, quad, varyingColor, alpha);
    }

}
