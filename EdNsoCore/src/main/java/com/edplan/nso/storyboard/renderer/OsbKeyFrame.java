package com.edplan.nso.storyboard.renderer;

import com.edplan.framework.ui.animation.Easing;

public class OsbKeyFrame<T>
{
	public final T startValue;
	public final T endValue;
	public final float startTime;
	public final float endTime;
	public final float duration;
	public final Easing easing;

	public OsbKeyFrame(T s,T e,float t,float d,Easing es){
		startValue=s;
		endValue=e;
		startTime=t;
		duration=d;
		endTime=t+d;
		easing=es;
	}
}
