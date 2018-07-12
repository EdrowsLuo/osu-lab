package com.edplan.nso.storyboard.renderer;
import com.edplan.framework.ui.animation.Easing;

public class OsbFloatKeyFrame
{
	public final float startValue;
	public final float endValue;
	public final float startTime;
	public final float endTime;
	public final float duration;
	public final Easing easing;
	
	public OsbFloatKeyFrame(float s,float e,float t,float d,Easing es){
		startValue=s;
		endValue=e;
		startTime=t;
		duration=d;
		endTime=t+d;
		easing=es;
	}
}
