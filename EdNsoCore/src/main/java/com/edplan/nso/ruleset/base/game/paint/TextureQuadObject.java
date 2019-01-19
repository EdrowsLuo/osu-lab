package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.utils.FloatRef;
import com.edplan.nso.ruleset.base.game.World;

public class TextureQuadObject extends AdvancedDrawObject implements IAnimateObject{

    public TextureQuad sprite;

    public TextureQuadObject() {
        sprite = new TextureQuad();
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().add(sprite);
    }

    @Override
    public Vec2 getPositionRef() {
        return sprite.position;
    }

    @Override
    public Color4 getColorRef() {
        return sprite.accentColor;
    }

    @Override
    public Vec2 getScaleRef() {
        return sprite.scale;
    }

    @Override
    public FloatRef getAlphaRef() {
        return sprite.alpha;
    }

    @Override
    public FloatRef getRotationRef() {
        return sprite.rotation;
    }
}
