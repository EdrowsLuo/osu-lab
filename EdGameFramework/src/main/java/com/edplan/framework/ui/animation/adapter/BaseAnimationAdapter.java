package com.edplan.framework.ui.animation.adapter;

import com.edplan.framework.ui.animation.IAnimationAdapter;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.interfaces.InvokeSetter;
import com.edplan.framework.ui.animation.interpolate.ValueInterpolator;

public class BaseAnimationAdapter<T, V> implements IAnimationAdapter<T, V> {
    private InvokeSetter<T, V> setter;

    private ValueInterpolator<V> interpolator;

    public BaseAnimationAdapter(InvokeSetter<T, V> setter, ValueInterpolator<V> inp) {
        this.setter = setter;
        this.interpolator = inp;
    }


    @Override
    public void apply(T target, V startValue, V endValue, double time, Easing easing) {

        setter.invoke(target, interpolator.applyInterplate(startValue, endValue, time, easing));
    }
}
