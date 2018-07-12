package com.edplan.framework.graphics.line;

import com.edplan.framework.math.Vec2;

public interface AbstractPath 
{
	public Vec2 get(int idx);
	
	public int size();
	
	public float getWidth();
}
