package com.edplan.framework.graphics.opengl.batch;

public interface BaseBatch<T>
{
	
	public void add(T t);
	
	public void add(T... ts);
	
	public int getVertexCount();
	
	public void clear();
}
