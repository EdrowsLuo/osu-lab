package com.edplan.framework.ui.animation;

public abstract class AbstractAnimation implements AnimationCallback
{
	private int loopCount=0;
	
	/**
	 *动画的持续时间，对于循环的动画而言这里的时间为一个循环的时间。
	 *单位ms
	 */
	public abstract double getDuration();
	
	/**
	 *动画的循环方式
	 */
	public abstract LoopType getLoopType();

	/**
	 *动画的状态，默认为等待，需要后续操作让动画实际进行
	 */
	public abstract AnimState getState();

	/**
	 *设置当前进度
	 *@param p: 设置的进度，0～getDuration()之间的int。
	 */
	public abstract void setProgressTime(double p);
	
	public abstract void postProgressTime(double deltaTime);
	
	/**
	 *获取当前状态下的progressTime
	 */
	public abstract double getProgressTime();
	
	public abstract void dispos();
	
	public abstract void start();
	
	/**
	 *对于循环的Animation，获取现在循环的次数。
	 *初始值为0（对于不循环的一直为0）
	 */
	public int getLoopCount(){
		return loopCount;
	}
	/**
	 *这个方法不要主动调用，是暴露给Handler的一个方法
	 */
	public void addLoopCount(){
		loopCount++;
	}
	
	public boolean isReversing(){
		return getLoopType().isReverse(getLoopCount());
	}
}
