package com.edplan.nso;

public class NsoException extends Exception
{
	public NsoException(String msg){
		super(msg);
	}
	
	public NsoException(String msg,Throwable caus){
		super(msg,caus);
	}
}
