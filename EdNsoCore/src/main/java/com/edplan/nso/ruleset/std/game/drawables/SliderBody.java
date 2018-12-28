package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.paint.AdvancedDrawObject;

public class SliderBody extends AdvancedDrawObject {

    private LinePath path;

    public void setPath(LinePath path) {
        this.path = path;
    }

    public LinePath getPath() {
        return path;
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {

    }

}
