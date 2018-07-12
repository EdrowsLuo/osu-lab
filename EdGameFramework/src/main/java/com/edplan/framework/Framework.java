package com.edplan.framework;

public class Framework
{
	
	private static final int frameworkVersion=1;
	
	public static int getFrameworkVersion(){
		return frameworkVersion;
	}
	
	
	/**
	 *获取相对的精确时间
	 */
	public static double relativePreciseTimeMillion(){
		return System.nanoTime()/1000000d;
	}
	
	public static int msToNm(double ms){
		return (int)(ms*1000000);
	}
	
	public static long absoluteTimeMillion(){
		return System.currentTimeMillis();
	}
}
