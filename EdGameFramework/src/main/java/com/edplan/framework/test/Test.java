package com.edplan.framework.test;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Test
{
	public static final String IS_RELEASE="is_release";
	
	private static Test test;
	
	private HashMap<String,List<String>> values;
	
	public Test(){
		values=new HashMap<String,List<String>>();
	}
	
	public boolean getBoolean(String tag,boolean def){
		if(values.containsKey(tag)){
			return Boolean.parseBoolean(values.get(tag).get(0));
		}else{
			setBoolean(tag,def);
			return getBoolean(tag,def);
		}
	}
	
	public void setBoolean(String tag,boolean value){
		if(values.containsKey(tag)){
			values.get(tag).set(0,Boolean.toString(value));
		}else{
			values.put(tag,new ArrayList<String>());
			values.get(tag).add(Boolean.toString(value));
		}
	}
	
	public static Test get(){
		if(test==null)test=new Test();
		return test;
	}
	
}
