package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.nso.ruleset.base.game.paint.AdvancedDrawObject;

public abstract class BasePiece extends AdvancedDrawObject {

    public abstract void initialBaseScale(float scale);

    public abstract void initialAccentColor(Color4 color);

    public void expire(double time) {
        detach();
    }

}
