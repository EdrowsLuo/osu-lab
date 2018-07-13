package com.edplan.nso.storyboard.elements;

import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.animation.LoopType;
import com.edplan.nso.storyboard.Storyboard;
import com.edplan.nso.storyboard.elements.drawable.BaseDrawableSprite;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.nso.storyboard.elements.drawable.AnimationDrawableSprite;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public class StoryboardAnimation extends StoryboardSprite {
    private int frameCount;
    private double frameDelay;
    private Storyboard.AnimationLoopType loopType;

    public StoryboardAnimation(String path, Anchor anchor, Vec2 initialPosition, int frameCount, double frameDelay, Storyboard.AnimationLoopType loopType) {
        super(path, anchor, initialPosition);
        this.frameCount = frameCount;
        this.frameDelay = frameDelay;
        this.loopType = loopType;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameDelay(double frameDelay) {
        this.frameDelay = frameDelay;
    }

    public double getFrameDelay() {
        return frameDelay;
    }

    public void setLoopType(Storyboard.AnimationLoopType loopType) {
        this.loopType = loopType;
    }

    public Storyboard.AnimationLoopType getLoopType() {
        return loopType;
    }

    public String buildPath(int index) {
        return getPath().substring(0, getPath().lastIndexOf(".")) + index + getPath().substring(getPath().lastIndexOf("."), getPath().length());
    }

    @Override
    public void initialTexture(BaseDrawableSprite sprite, PlayingStoryboard storyboard) {

        //super.initialTexture(sprite, storyboard);
        AnimationDrawableSprite asp = (AnimationDrawableSprite) sprite;
        AbstractTexture[] l = new AbstractTexture[frameCount];
        for (int i = 0; i < l.length; i++) {
            l[i] = storyboard.getTexturePool().getTexture(buildPath(i));
        }
        asp.setTextures(l);
    }

    @Override
    public String getInitialPath() {

        return buildPath(0);
    }

    @Override
    public String[] getTexturePaths() {

        String[] s = new String[frameCount];
        for (int i = 0; i < s.length; i++) {
            s[i] = buildPath(i);
        }
        return s;
    }

    @Override
    public BaseDrawableSprite createDrawable(PlayingStoryboard storyboard) {

        return new AnimationDrawableSprite(storyboard, this);
    }
}
