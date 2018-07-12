package com.edplan.superutils;

public class TestClock
{
	public long startTime;
	
	public long nowTime;
	
	public TestClock(){
		start();
	}
	
	public void  start(){
		startTime=System.currentTimeMillis();
	}
	
	public void end(){
		nowTime=System.currentTimeMillis();
	}
	
	public int getTime(){
		return (int)(nowTime-startTime);
	}
	
}
