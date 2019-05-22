package com.edlplan.nso.ruleset.base.game.paint;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.timing.IHasIntervalSchedule;
import com.edlplan.framework.timing.IntervalSchedule;
import com.edlplan.framework.timing.TimeUpdateable;
import com.edlplan.framework.utils.Lazy;
import com.edlplan.framework.utils.annotation.NotThreadSafe;

public abstract class AdvancedDrawObject extends DrawObject implements IHasIntervalSchedule {

    private Lazy<IntervalSchedule> intervalScheduleLazy = Lazy.create(IntervalSchedule::new);

    protected abstract void onDraw(BaseCanvas canvas, World world);

    @Override
    public IntervalSchedule getIntervalSchedule() {
        return intervalScheduleLazy.get();
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
