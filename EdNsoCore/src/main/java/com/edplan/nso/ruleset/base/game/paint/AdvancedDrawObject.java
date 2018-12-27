package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.timing.IntervalSchedule;
import com.edplan.framework.timing.TimeUpdateable;
import com.edplan.framework.utils.Lazy;
import com.edplan.framework.utils.annotation.NotThreadSafe;
import com.edplan.nso.ruleset.base.game.World;

public abstract class AdvancedDrawObject extends DrawObject {

    private Lazy<IntervalSchedule> intervalScheduleLazy = Lazy.create(IntervalSchedule::new);

    protected abstract void onDraw(BaseCanvas canvas, World world);

    public IntervalSchedule getIntervalSchedule() {
        return intervalScheduleLazy.get();
    }

    @NotThreadSafe
    public void addIntervalTask(double start, double end, TimeUpdateable updateable) {
        getIntervalSchedule().addTask(start, end, false, updateable);
    }

    @NotThreadSafe
    public void addAnimTask(double start, double duration, TimeUpdateable anim) {
        getIntervalSchedule().addAnimTask(start, duration, anim);
    }

    @NotThreadSafe
    public void addTask(double time, Runnable runnable) {
        getIntervalSchedule().addTask(time, time, true, time1 -> runnable.run());
    }

    @Override
    public void draw(BaseCanvas canvas, World world) {
        handleOperations();
        if (!intervalScheduleLazy.isEmpty()) {
            intervalScheduleLazy.get().update(world.getPaintFrameTime());
        }
        if (isAttached()) {
            onDraw(canvas,world);
        }
    }
}
