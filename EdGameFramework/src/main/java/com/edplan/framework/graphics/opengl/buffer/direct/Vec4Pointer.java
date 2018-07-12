package com.edplan.framework.graphics.opengl.buffer.direct;
import com.edplan.framework.graphics.opengl.objs.Color4;

public abstract class Vec4Pointer
{
	public void set(Color4 c){
		set(c.r,c.g,c.b,c.a);
	}
	
	public abstract void set(float r,float g,float b,float a);
}
