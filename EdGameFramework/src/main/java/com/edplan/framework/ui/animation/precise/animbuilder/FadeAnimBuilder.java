package com.edplan.framework.ui.animation.precise.animbuilder;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.animation.precise.advance.FadePreciseAnimation;

public class FadeAnimBuilder
{
	private PreciseTimeline timeLine;
	
	private FadePreciseAnimation anim=new FadePreciseAnimation();
	
	public FadeAnimBuilder(PreciseTimeline timeLine){
		this.timeLine=timeLine;
		anim.setStartTime(timeLine.frameTime());
	}
	
	public FadeAnimBuilder duration(int d){
		anim.setDuration(d);
		return this;
	}
	
	public FadeAnimBuilder startTime(int t){
		anim.setStartTime(t);
		return this;
	}
	
	public FadeAnimBuilder fade(int duration,float sv,float ev){
		anim.setDuration(duration);
		anim.setStartValue(sv);
		anim.setEndValue(ev);
		return this;
	}
	
	public FadeAnimBuilder fadeTo(int duration,float v){
		return fade(duration,anim.getTarget().getAlpha(),v);
	}
	
	public FadeAnimBuilder fadeIn(int duration){
		return fadeTo(duration,1);
	}
	
	public FadeAnimBuilder fadeOut(int duration){
		return fadeTo(duration,0);
	}

	public FadeAnimBuilder timeLine(PreciseTimeline timeLine) {
		this.timeLine=timeLine;
		return this;
	}

	public PreciseTimeline getTimeLine() {
		return timeLine;
	}

	public void setAnim(FadePreciseAnimation anim) {
		this.anim=anim;
	}

	public FadePreciseAnimation getAnim() {
		return anim;
	}
	
	public void post(){
		anim.post(timeLine);
	}
}
