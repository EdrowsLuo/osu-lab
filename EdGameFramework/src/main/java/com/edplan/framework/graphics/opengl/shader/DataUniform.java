package com.edplan.framework.graphics.opengl.shader;

public abstract class DataUniform<T>
{
	protected final int handle;
	
	protected final boolean available;
	
	public DataUniform(int handle){
		this.handle=handle;
		this.available=(handle!=-1);
	}
	
	public abstract void loadData(T t);
	
	public int getHandle(){
		return handle;
	}
}
