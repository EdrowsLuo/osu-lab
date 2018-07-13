package com.edplan.framework.ui.animation.interpolate;

import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.animation.Easing;

public class Vec2Interpolator implements ValueInterpolator<Vec2> {
    public static Vec2Interpolator Instance = new Vec2Interpolator();

    public Vec2 buffer = new Vec2();

    @Override
    public Vec2 applyInterplate(Vec2 startValue, Vec2 endValue, double time, Easing easing) {

        double inp = EasingManager.apply(easing, time);
        float x = (float) (startValue.x * (1 - inp) + endValue.x * inp);
        float y = (float) (startValue.y * (1 - inp) + endValue.y * inp);
        buffer.set(x, y);
        return buffer;
    }
}
