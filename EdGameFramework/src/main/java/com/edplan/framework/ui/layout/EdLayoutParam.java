package com.edplan.framework.ui.layout;

public class EdLayoutParam
{
	public static final long DEFAULT_SIZE_PARAM;
	
	public static final int DEFAULT_GRAVITY=0;
	
	static{
		DEFAULT_SIZE_PARAM=Param.makeupParam(0,Param.WRAP_CONTENT);
	}

	public long width;
	public long height;
	public float xoffset;
	public float yoffset;
	
	public EdLayoutParam(){
		width=DEFAULT_SIZE_PARAM;
		height=DEFAULT_SIZE_PARAM;
	}
	
	public EdLayoutParam(EdLayoutParam l){
		width=l.width;
		height=l.height;
		xoffset=l.xoffset;
		yoffset=l.yoffset;
	}
}
