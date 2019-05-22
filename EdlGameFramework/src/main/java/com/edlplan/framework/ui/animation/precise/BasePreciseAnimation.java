package com.edlplan.framework.ui.animation.precise;

import com.edlplan.framework.timing.PreciseTimeline;
import com.edlplan.framework.ui.animation.AnimState;

public class BasePreciseAnimation extends AbstractPreciseAnimation {
    private double startTime;

    private double duration;

    private double progressTime;

    private AnimState state = AnimState.Waiting;

    public void start() {
        state = AnimState.Running;
        setProgressTime(0);
    }

    public void post(PreciseTimeline timeline) {
		/*
		double endTime=getEndTime();
		if(endTime<=timeline.frameTime()){
			//如果当前时间已经晚于frameTime了，直接模拟一次生命周期
			onStart();
			setProgressTime(getDuration());
			onFinish();
			onEnd();
		}else{
			*/

        timeline.addAnimation(this);
        start();
        //}
        //timeline.frameTime()-getStartTimeAtTimeline());
    }

    public double currentTime() {
        return getStartTimeAtTimeline() + getProgressTime();
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    protected void postTime(double deltaTime, double progressTime) {
        seekToTime(progressTime);
    }

    protected void seekToTime(double progressTime) {

    }

    @Override
    public double getDuration() {

        return duration;
    }

    @Override
    public AnimState getState() {

        return state;
    }

    @Override
    public void setProgressTime(double p) {

        progressTime = p;
        seekToTime(p);
    }

    @Override
    public void postProgressTime(double deltaTime) {

        progressTime += deltaTime;
        postTime(deltaTime, progressTime);
    }

    @Override
    public double getProgressTime() {

        return progressTime;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(double p) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onEnd() {

    }

    @Override
    public double getStartTimeAtTimeline() {

        return startTime;
    }

    @Override
    public void dispos() {

    }
}
