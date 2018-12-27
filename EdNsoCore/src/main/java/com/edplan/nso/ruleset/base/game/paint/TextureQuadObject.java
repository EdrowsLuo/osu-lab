package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.ui.drawable.sprite.TextureSprite;
import com.edplan.nso.ruleset.base.game.World;

public class TextureQuadObject extends AdvancedDrawObject{

    public TextureQuad sprite;

    public TextureQuadObject(MContext context) {
        sprite = new TextureQuad();
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().add(sprite);
    }
}
