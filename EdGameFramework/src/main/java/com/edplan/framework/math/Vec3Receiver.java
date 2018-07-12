package com.edplan.framework.math;

public interface Vec3Receiver
{
	public void receive(float x,float y,float z);
	
	public void receive(Vec3 v);
	
	public void receive(Vec2 v,float z);
}
