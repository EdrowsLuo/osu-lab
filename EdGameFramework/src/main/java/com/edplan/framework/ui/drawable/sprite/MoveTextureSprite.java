package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLCanvas2D;

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
