package com.edlplan.nso.ruleset.base.game.paint;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;

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
