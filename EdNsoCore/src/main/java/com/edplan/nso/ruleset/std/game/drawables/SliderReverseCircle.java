package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.paint.AdvancedDrawObject;

public class SliderReverseCircle extends AdvancedDrawObject{

    public TextureQuad textureQuad = new TextureQuad()
            .enableRotation()
            .enableScale();


    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().add(textureQuad);
    }

}
