package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.MultipleTextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.nso.ruleset.base.game.World;

public class AnimateTextureQuadObject extends AdvancedDrawObject{

    public MultipleTextureQuad sprite;

    private double startTime;

    private double frameDuration;

    public AnimateTextureQuadObject() {
        sprite = new MultipleTextureQuad();
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setFrameDuration(double frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void setCycleDuration(double cycleDuration) {
        this.frameDuration = cycleDuration / sprite.textureEntries.length;
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        double dt = world.getPaintFrameTime() - startTime;
        if (dt > 0) {
            sprite.switchTexture((int) (dt / frameDuration));
        }
        TextureQuadBatch.getDefaultBatch().add(sprite);
    }

}
