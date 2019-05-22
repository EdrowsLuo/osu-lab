package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.graphics.opengl.GLCanvas2D;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;

public class MoveTextureSprite extends TextureSprite {

    public MoveTextureSprite(MContext c) {
        super(c);
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {
        super.prepareShader(canvas);
        getShader().uBackbuffer.loadData(((GLCanvas2D) canvas).getLayer().getTexture().getTexture());
    }

    @Override
    protected MoveTextureSpriteShader getShader() {
        return MoveTextureSpriteShader.get();
    }
}
