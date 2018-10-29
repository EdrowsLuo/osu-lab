package com.edplan.framework.ui.animation.interpolate;

import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.ui.animation.Easing;

public class InvalidInterpolator<T> implements ValueInterpolator<T> {
    public static InvalidInterpolator<Boolean> ForBoolean = new InvalidInterpolator<Boolean>();
    public static InvalidInterpolator<BlendType> ForBlendType = new InvalidInterpolator<BlendType>();

    @Override
    public T applyInterplate(T startValue, T endValue, double time, Easing easing) {

        return startValue;
    }
}
