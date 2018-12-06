package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.ui.drawable.sprite.TextureSprite;
import com.edplan.nso.ruleset.base.game.World;

public class TextureRect extends AdvancedDrawObject{

    private TextureSprite sprite;

    public TextureRect(MContext context) {
        sprite = new TextureSprite(context);
    }

    public void setTexture(AbstractTexture texture) {
        sprite.setTexture(texture);
    }

    public void setQuad(IQuad quad) {
        sprite.setArea(quad);
    }

    public TextureSprite getSprite() {
        return sprite;
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        sprite.draw(canvas);
    }
}
