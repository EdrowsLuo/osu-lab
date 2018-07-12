package com.edplan.framework.ui.inputs;

/**
 *滚动事件，当在一个接受滚动的view上移动一定距离后被触发
 */
public class ScrollEvent
{
	public static final int DIRECTION_HORIZON=1;
	public static final int DIRECTION_VERTICAL=2;
	
	public static final int STATE_START=1;
	public static final int STATE_SCROLLING=2;
	public static final int STATE_END=3;
	public static final int STATE_CANCEL=4;
	
	private int scrollFlag;
	private float scrollX,scrollY;
	private int state;
	private double time;
	private double deltaTime;

	public void setDeltaTime(double deltaTime){
		this.deltaTime=deltaTime;
	}

	public double getDeltaTime(){
		return deltaTime;
	}

	public void setTime(double time){
		this.time=time;
	}

	public double getTime(){
		return time;
	}


	public void addScrollFlag(int scrollFlag){
		this.scrollFlag|=scrollFlag;
	}

	public int getScrollFlag(){
		return scrollFlag;
	}

	public void setScrollX(float scrollX){
		this.scrollX=scrollX;
	}

	public float getScrollX(){
		return scrollX;
	}

	public void setScrollY(float scrollY){
		this.scrollY=scrollY;
	}

	public float getScrollY(){
		return scrollY;
	}

	public void setState(int state){
		this.state=state;
	}

	public int getState(){
		return state;
	}
}
