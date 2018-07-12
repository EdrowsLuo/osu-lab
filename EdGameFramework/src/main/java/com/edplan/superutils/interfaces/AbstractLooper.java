package com.edplan.superutils.interfaces;

public interface AbstractLooper<T extends Loopable>
{
	public void loop(double deltaTime);
	
	public void prepare();
	
	public void addLoopable(T l);
}
