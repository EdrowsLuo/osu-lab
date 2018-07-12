package com.edplan.nso.parser;
import java.util.TreeMap;

public class ParserSetting
{
	private TreeMap<String,Object> settings=new TreeMap<String,Object>();
	
	public Object getSetting(String key){
		return settings.get(key);
	}
	
	public int getInt(String key,int defValue){
		Object obj=getSetting(key);
		if(obj!=null){
			return (Integer)obj;
		}else{
			return defValue;
		}
	}
	
	public String getString(String key){
		Object obj=getSetting(key);
		if(obj!=null){
			return (String)obj;
		}else{
			return "";
		}
	}
}
