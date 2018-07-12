package com.edplan.framework.utils;
import java.util.Map;
import android.util.Log;
import java.util.TreeMap;

public class MLog
{
	public static class test{
		public static Map<String,Boolean> data=new TreeMap<String,Boolean>();
		
		public static boolean getBoolean(String key,boolean def){
			if(data.containsKey(key)){
				return data.get(key);
			}else{
				data.put(key,def);
				return def;
			}
		}
		
		public static void putBoolean(String tag,boolean v){
			data.put(tag,v);
		}
		
		public static void vOnce(String tag,String head,String msg){
			if(!getBoolean(tag,false)){
				putBoolean(tag,true);
				Log.v(head,msg);
			}
		}
		
		public static void runOnce(String tag,Runnable r){
			if(!getBoolean(tag,false)){
				putBoolean(tag,true);
				r.run();
			}
		}
	}
}
