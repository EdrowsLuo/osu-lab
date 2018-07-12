package com.edplan.framework.timing;

public class AdjustableTimeline extends PreciseTimeline
{
	private float timeRate=1;
	
	private int curRealTime=0;
	
	private int preRealPostTime=0;
	
	private int realPostTime=0;
	
	public AdjustableTimeline(float r){
		timeRate=r;
	}

	public void setTimeRate(float timeRate) {
		this.timeRate=timeRate;
	}

	public float getTimeRate() {
		return timeRate;
	}
	
	@Override
	public void onLoop(double deltaTime) {

		//curRealTime+=deltaTime;
		//realPostTime=(int)(curRealTime*timeRate);
		//super.onLoop(realPostTime-preRealPostTime);
		//preRealPostTime=realPostTime;
		super.onLoop(deltaTime*timeRate);
	}
}
