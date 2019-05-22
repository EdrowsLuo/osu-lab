package com.edlplan.framework.ui.animation;

public interface IAnimationAdapter<T, V> {
    public void apply(T target, V startValue, V endValue, double time, Easing easing);
}
