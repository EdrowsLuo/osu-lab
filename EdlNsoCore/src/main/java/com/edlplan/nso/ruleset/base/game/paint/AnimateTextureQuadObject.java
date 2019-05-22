package com.edlplan.nso.ruleset.base.game.paint;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.batch.v2.object.MultipleTextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;

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
