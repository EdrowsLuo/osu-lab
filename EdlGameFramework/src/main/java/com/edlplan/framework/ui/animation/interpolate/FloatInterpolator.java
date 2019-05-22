package com.edlplan.framework.ui.animation.interpolate;

import com.edlplan.framework.ui.animation.Easing;

public class FloatInterpolator implements ValueInterpolator<Float> {
    public static FloatInterpolator Instance = new FloatInterpolator();

    @Override
    public Float applyInterplate(Float startValue, Float endValue, double time, Easing easing) {

        double inp = EasingManager.apply(easing, time);
        return (float) (startValue * (1 - inp) + endValue * inp);
    }
}
