package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.nso.ruleset.base.game.World;

/**
 * 管理绘制的类，在GL线程里
 */
public class PaintWorld {

    private World world;

    private GroupDrawObject rootGroup = new GroupDrawObject(){
        @Override
        public boolean isAttached() {
            return true;
        }
    };

    public PaintWorld(World world) {
        this.world = world;
    }

    public void addDrawObject(DrawObject object) {
        rootGroup.attachFront(object);
    }

    public void addDrawObjects(DrawObject... objects) {
        for (DrawObject object : objects) addDrawObject(object);
    }

    public void draw(BaseCanvas canvas) {
        rootGroup.draw(canvas, world);
    }

}
