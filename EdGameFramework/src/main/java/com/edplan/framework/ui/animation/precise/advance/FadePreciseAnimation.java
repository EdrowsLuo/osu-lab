package com.edplan.framework.ui.animation.precise.advance;

import com.edplan.framework.ui.drawable.interfaces.IFadeable;
import com.edplan.framework.ui.animation.AnimationHelper;

public class FadePreciseAnimation extends AdvancePreciseAnimation {
    private IFadeable target;

    private float startValue;

    private float endValue;

    public FadePreciseAnimation() {

    }

    public void setTarget(IFadeable target) {
        this.target = target;
    }

    public IFadeable getTarget() {
        return target;
    }

    public void setStartValue(float startValue) {
        this.startValue = startValue;
    }

    public float getStartValue() {
        return startValue;
    }

    public void setEndValue(float endValue) {
        this.endValue = endValue;
    }

    public float getEndValue() {
        return endValue;
    }

    @Override
    public void setValueProgress(float fp) {

        super.setValueProgress(fp);
        float v = startValue * (1 - fp) + endValue * fp;
        target.setAlpha(v);
    }
}
