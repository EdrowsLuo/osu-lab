package com.edplan.framework.ui.animation.precise.advance;

import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.drawable.interfaces.IScaleable2D;

public class ScalePreciseAnimation extends AdvancePreciseAnimation {
    private IScaleable2D target;

    private Vec2 startScale = new Vec2(1, 1);

    private Vec2 endScale = new Vec2(1, 1);

    public void setTarget(IScaleable2D target) {
        this.target = target;
    }

    public IScaleable2D getTarget() {
        return target;
    }

    public void setStartScale(Vec2 startScale) {
        this.startScale.set(startScale);
    }

    public Vec2 getStartScale() {
        return startScale;
    }

    public void setEndScale(Vec2 endScale) {
        this.endScale.set(endScale);
    }

    public Vec2 getEndScale() {
        return endScale;
    }

    @Override
    public void setValueProgress(float fp) {

        super.setValueProgress(fp);
        Vec2 itp = startScale.copy().zoom(1 - fp).add(endScale.copy().zoom(fp));
        target.setScale(itp.x, itp.y);
    }
}
