package com.edplan.superutils;
import java.util.HashMap;

public class DefUtil
{
	private static HashMap<Class,Object> defValueMap=new HashMap<Class,Object>();
	
	
	
	public static <T> T getDefValue(Class<T> klass){
		return (T)defValueMap.get(klass);
	}
	
}
