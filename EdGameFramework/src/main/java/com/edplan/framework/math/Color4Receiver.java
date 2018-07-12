package com.edplan.framework.math;
import com.edplan.framework.graphics.opengl.objs.Color4;

public interface Color4Receiver
{
	public void receive(float r,float g,float b,float a);
	
	public void receive(Color4 color);
}
