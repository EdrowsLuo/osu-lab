package com.edlplan.nso.storyboard.elements.drawable;

import com.edlplan.nso.storyboard.PlayingStoryboard;
import com.edlplan.nso.storyboard.Storyboard;
import com.edlplan.nso.storyboard.elements.StoryboardAnimation;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;

public class AnimationDrawableSprite extends BaseDrawableSprite {
    private double startTime;
    private int frameCount;
    private double frameDelay;
    private Storyboard.AnimationLoopType loopType;
    private AbstractTexture[] textures;

    public AnimationDrawableSprite(PlayingStoryboard storyboard, StoryboardAnimation sprite) {
        super(storyboard, sprite);
        frameCount = sprite.getFrameCount();
        frameDelay = sprite.getFrameDelay();
        loopType = sprite.getLoopType();
        startTime = sprite.getStartTime();
    }

    public void setTextures(AbstractTexture[] textures) {
        this.textures = textures;
    }

    @Override
    public void prepareForDraw() {

        super.prepareForDraw();
        int idx = (int) (Math.max(0, getTimeline().frameTime() - startTime) / frameDelay);
        if (idx >= frameCount) {
            switch (loopType) {
                case LoopOnce:
                    idx = frameCount - 1;
                    break;
                case LoopForever:
                default:
                    idx %= frameCount;
            }
        }
        texture = textures[idx];
    }
}
