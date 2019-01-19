package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.nso.ruleset.base.game.World;

public class SimpleTextureQuadObject extends DrawObject {

    public TextureQuad sprite;

    public SimpleTextureQuadObject() {
        sprite = new TextureQuad();
    }

    @Override
    public void draw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().add(sprite);
    }
}
