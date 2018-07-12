package com.edplan.framework.math.its;
import com.edplan.framework.math.Vec2;

public interface IVec2
{
	public float getX();
	
	public float getY();
	
	public void setX(float v);
	
	public void setY(float v);
	
	public void set(float xv,float yv);
	
	public void set(Vec2 v);
}
