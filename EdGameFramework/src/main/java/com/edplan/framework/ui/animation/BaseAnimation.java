package com.edplan.framework.ui.animation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.animation.callback.OnEndListener;
import com.edplan.framework.ui.animation.callback.OnProgressListener;

public class BaseAnimation extends AbstractAnimation
{
	private OnFinishListener onFinishListener;
	
	private OnEndListener onEndListener;
	
	private OnProgressListener onProgressListener;
	
	private double progress;
	
	private double duration;
	
	private LoopType loopType=LoopType.None;
	
	private AnimState state=AnimState.Waiting;

	public void setOnProgressListener(OnProgressListener onProgressListener){
		this.onProgressListener=onProgressListener;
	}

	public OnProgressListener getOnProgressListener(){
		return onProgressListener;
	}

	public void setOnEndListener(OnEndListener onEndListener){
		this.onEndListener=onEndListener;
	}

	public OnEndListener getOnEndListener(){
		return onEndListener;
	}

	protected void setState(AnimState state){
		this.state=state;
	}

	public void setLoopType(LoopType loopType){
		this.loopType=loopType;
	}

	public void setDuration(double duration){
		this.duration=duration;
	}

	public void setOnFinishListener(OnFinishListener onFinishListener){
		this.onFinishListener=onFinishListener;
	}

	public OnFinishListener getOnFinishListener(){
		return onFinishListener;
	}
	
	public void start(){
		setState(AnimState.Running);
	}
	
	@Override
	public void onStart(){

	}

	@Override
	public void onProgress(double p){

		if(onProgressListener!=null)onProgressListener.onProgress(p);
	}

	@Override
	public void onFinish(){

		state=AnimState.Stop;
		if(onFinishListener!=null)onFinishListener.onFinish();
	}

	@Override
	public void onEnd(){

		state=AnimState.Stop;
		if(onEndListener!=null)onEndListener.onEnd();
	}

	@Override
	public double getDuration(){

		return duration;
	}

	@Override
	public LoopType getLoopType(){

		return loopType;
	}

	@Override
	public AnimState getState(){

		return state;
	}

	@Override
	public void setProgressTime(double p){

		progress=p;
	}

	@Override
	public void postProgressTime(double deltaTime){

		progress+=deltaTime;
	}

	@Override
	public double getProgressTime(){

		return progress;
	}

	@Override
	public void dispos(){

	}
}
