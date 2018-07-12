package com.edplan.framework.ui.animation;

public class AnimationHelper
{
	public static float getFloatProgress(double progressTime,double duration){
		if(duration==0){
			return 1;
		}else{
			return (float)(progressTime/duration);
		}
	}
}
