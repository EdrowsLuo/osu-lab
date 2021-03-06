package com.edlplan.nso.ruleset.base.game.paint;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.timing.Schedule;

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

    public void scheduleAttachFront(double time, DrawObject object) {
        addEvent(time, () -> attachFront(object));
    }

    public void scheduleAttachBehind(double time, DrawObject object) {
        addEvent(time, () -> attachBehind(object));
    }

    public void scheduleAttachFrontAll(double time, DrawObject... objects) {
        addEvent(time, () -> {
            for (DrawObject object : objects) {
                attachFront(object);
            }
        });
    }

    public void scheduleAttachBehindAll(double time, DrawObject... objects) {
        addEvent(time, () -> {
            for (DrawObject object : objects) {
                attachBehind(object);
            }
        });
    }
}
