package com.edplan.framework.ui.animation.interpolate;
import com.edplan.framework.ui.animation.Easing;

public interface ValueInterpolator<T>
{
	public T applyInterplate(T startValue,T endValue,double time,Easing easing);
}
