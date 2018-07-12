package com.edplan.superutils.classes.advance;

public interface IRunnableHandler
{
	public void post(Runnable r);
	
	public void post(Runnable r,double delayMS);
}
