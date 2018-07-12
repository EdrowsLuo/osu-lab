package com.edplan.framework.graphics.opengl.buffer.direct;

public abstract class Vec3Pointer
{
	
	public void set(float xv,float yv){
		set(xv,yv,0);
	}
	
	public abstract void set(float xv,float yv,float zv);
}
