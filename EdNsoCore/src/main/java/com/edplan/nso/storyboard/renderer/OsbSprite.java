package com.edplan.nso.storyboard.renderer;

import com.edplan.nso.storyboard.elements.StoryboardSprite;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.utils.BitUtil;
import com.edplan.nso.storyboard.elements.Selecters;
import com.edplan.nso.storyboard.renderer.OsbRenderer.*;
import com.edplan.framework.ui.animation.Easing;

public class OsbSprite {
    protected final OsbQuad spriteQuad = new OsbQuad();

    protected final Anchor anchor;

    protected AbstractTexture texture;

    protected short flip = 0;

    protected BlendType blendType = BlendType.Normal;

    protected OsbFloatKeyFrameQuery PositionX, PositionY, ScaleX, ScaleY, Rotation, Alpha;

    protected OsbFloatKeyFrameQuery[] querys;

    protected OsbKeyFrameQuery<Color4> Color;

    protected OsbKeyFrameQuery<Boolean> FlipH, FlipV;

    protected OsbKeyFrameQuery<BlendType> Blend;

    protected final StoryboardSprite rawSprite;

    public final float startTime, endTime, duration;

    public OsbSprite(StoryboardSprite sprite) {
        rawSprite = sprite;
        startTime = (float) sprite.getStartTime();
        endTime = (float) sprite.getEndTime();
        duration = endTime - startTime;
        anchor = sprite.getAnchor();

        PositionX = new OsbFloatKeyFrameQuery(
                sprite.getCommands(Selecters.SX),
                spriteQuad.PositionX,
                startTime,
                sprite.getInitialPosition().x,
                true);
        PositionY = new OsbFloatKeyFrameQuery(
                sprite.getCommands(Selecters.SY),
                spriteQuad.PositionY,
                startTime,
                sprite.getInitialPosition().y,
                true);
        ScaleX = new OsbFloatKeyFrameQuery(
                sprite.getCommands(Selecters.SScaleX),
                spriteQuad.ScaleX,
                startTime,
                1,
                true);
        ScaleY = new OsbFloatKeyFrameQuery(
                sprite.getCommands(Selecters.SScaleY),
                spriteQuad.ScaleY,
                startTime,
                1,
                true);
        Rotation = new OsbFloatKeyFrameQuery(
                sprite.getCommands(Selecters.SRotation),
                spriteQuad.Rotation,
                startTime,
                1,
                true);
        Alpha = new OsbFloatKeyFrameQuery(
                sprite.getCommands(Selecters.SAlpha),
                spriteQuad.Alpha,
                startTime,
                1,
                true);
        Color = new OsbKeyFrameQuery<Color4>(
                sprite.getCommands(Selecters.SColour),
                spriteQuad.Color,
                startTime,
                Color4.White,
                true, false);

        FlipH = new OsbKeyFrameQuery<Boolean>(
                sprite.getCommands(Selecters.SFlipH),
                new IMainFrameHandler<Boolean>() {
                    @Override
                    public void update(Boolean startValue, Boolean endValue, double startTime, double duration, Easing easing) {

                        flip &= 2;
                        if (startValue) {
                            flip |= 1;
                        }
                    }
                },
                startTime,
                false,
                false, true);
        FlipV = new OsbKeyFrameQuery<Boolean>(
                sprite.getCommands(Selecters.SFlipV),
                new IMainFrameHandler<Boolean>() {
                    @Override
                    public void update(Boolean startValue, Boolean endValue, double startTime, double duration, Easing easing) {

                        flip &= 1;
                        if (startValue) {
                            flip |= 2;
                        }
                    }
                },
                startTime,
                false,
                false, true);
        Blend = new OsbKeyFrameQuery<BlendType>(
                sprite.getCommands(Selecters.SBlendType),
                new IMainFrameHandler<BlendType>() {
                    @Override
                    public void update(BlendType startValue, BlendType endValue, double startTime, double duration, Easing easing) {

                        blendType = startValue;
                    }
                },
                startTime,
                BlendType.Normal,
                false, true);

        querys = new OsbFloatKeyFrameQuery[]{PositionX, PositionY, ScaleX, ScaleY, Rotation};
    }

    public void loadTexture(PlayingStoryboard sb) {
        texture = sb.getTexturePool().getTexture(rawSprite.getInitialPath());
    }

    public void onLoadRenderer(OsbRenderer renderer) {
        spriteQuad.load(renderer);
    }

    public void updateTexture(float time) {

    }

    private AbstractTexture preDrawTexture;
    private short preFlip = 100;

    public void drawOsbRenderer(OsbRenderer renderer) {
        final float time = renderer.frameTime;
        Alpha.update(time);
        if (Alpha.isSmall(time)) {
            return;
        }
        updateTexture(time);
        Blend.update(time);
        renderer.setBlendType(blendType);
        renderer.setCurrentTexture(texture);
        for (OsbFloatKeyFrameQuery q : querys) {
            q.update(time);
        }
        Color.update(time);
        FlipH.update(time);
        FlipV.update(time);
        if (preDrawTexture != texture) {
            spriteQuad.updateAnchorOffset(texture, anchor);
            preDrawTexture = texture;
        }
        if (preFlip != flip) {
            spriteQuad.updateTextureCoord(texture.getRawQuad(), BitUtil.match(flip, 1), BitUtil.match(flip, 2));
            preFlip = flip;
        }
        spriteQuad.addToRenderer(renderer);
    }

    public void dispos() {
        spriteQuad.dispos();
        for (OsbFloatKeyFrameQuery f : querys) f.dispos();
        Alpha.dispos();
        Color.dispos();
        FlipH.dispos();
        FlipV.dispos();
        Blend.dispos();
    }
}
