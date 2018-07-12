package com.edplan.framework.ui.text.font.bmfont;

public class BMFontException extends RuntimeException
{
	public BMFontException(String msg){
		super(msg);
	}
	
	public BMFontException(String msg,Throwable e){
		super(msg,e);
	}
}
