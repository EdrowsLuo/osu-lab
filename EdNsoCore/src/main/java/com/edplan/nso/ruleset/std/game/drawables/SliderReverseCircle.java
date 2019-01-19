package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.nso.ruleset.base.game.World;

public class SliderReverseCircle extends CirclePiece{

    protected final TextureQuad reverseArrow = new TextureQuad()
            .syncPosition(position)
            .syncScale(scale)
            .syncAlpha(alpha)
            .enableRotation();

    public void initialReverseTexture(AbstractTexture texture) {
        reverseArrow.setTextureAndSize(texture);
    }

    @Override
    public void initialBaseScale(float scale) {
        super.initialBaseScale(scale);
        reverseArrow.size.zoom(scale);
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        super.onDraw(canvas, world);
        TextureQuadBatch.getDefaultBatch().add(reverseArrow);
    }
}
