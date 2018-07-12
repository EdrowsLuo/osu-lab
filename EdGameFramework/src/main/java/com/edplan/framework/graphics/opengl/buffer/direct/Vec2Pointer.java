package com.edplan.framework.graphics.opengl.buffer.direct;
import com.edplan.framework.math.Vec2;

public abstract class Vec2Pointer
{
	public void set(Vec2 v){
		set(v.x,v.y);
	}
	
	public abstract void set(float xv,float yv);
}
