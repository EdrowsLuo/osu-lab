package com.edplan.nso.resource;

public class OsuResException extends RuntimeException
{
	public OsuResException(String msg){
		super(msg);
	}
	
	public OsuResException(String msg,Throwable cause){
		super(msg,cause);
	}
}
