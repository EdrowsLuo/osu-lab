package com.edplan.framework.ui.animation.interpolate;

import com.edplan.framework.ui.animation.Easing;

public class RawFloatInterpolator 
{
	public static RawFloatInterpolator Instance=new RawFloatInterpolator();

	public float applyInterplate(Float startValue,Float endValue,double time,Easing easing) {

		double inp=EasingManager.apply(easing,time);
		return (float)(startValue*(1-inp)+endValue*inp);
	}
}
