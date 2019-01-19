package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.nso.ruleset.base.game.World;

public class StaticGroup extends AdvancedDrawObject {

    public final DrawObject[] group;

    public StaticGroup(int size) {
        group = new DrawObject[size];
    }

    @SuppressWarnings("unchecked")
    public <T extends DrawObject> T get(int i) {
        return (T) group[i];
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        final int size = group.length;
        for (int i = 0; i < size; i++) {
            group[i].draw(canvas, world);
        }
    }
}
