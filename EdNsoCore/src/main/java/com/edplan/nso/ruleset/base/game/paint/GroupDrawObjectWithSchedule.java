package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.timing.Schedule;
import com.edplan.nso.ruleset.base.game.World;

public class GroupDrawObjectWithSchedule extends GroupDrawObject {

    private Schedule schedule = new Schedule();

    @Override
    public void draw(BaseCanvas canvas, World world) {
        schedule.update(world.getPaintFrameTime());
        super.draw(canvas, world);
    }

    public void addEvent(double time, Runnable runnable){
        schedule.addEvent(time, runnable);
    }

}
