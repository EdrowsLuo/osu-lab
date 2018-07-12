package com.edplan.superutils;

import java.util.Map;
import java.util.TreeMap;

public class U
{
	public static final char NEXT_LINE='\n';
	
	public static String join(String[] list,String spl){
		if(list.length==1)return list[0];
		StringBuilder sb=new StringBuilder();
		for(int i=0,end=list.length-1;i<end;i++){
			sb.append(list[i]).append(spl);
		}
		sb.append(list[list.length-1]);
		return sb.toString();
	}
	
	public static StringBuilder nextLine(StringBuilder sb){
		return sb.append(NEXT_LINE);
	}
	
	public static StringBuilder appendProperty(StringBuilder sb,Object o1,Object o2){
		sb.append(o1).append(" : ").append(o2);
		return sb;
	}
	
	public static String[] divide(String res,int index){
		if(index<0||index>=res.length())return null;
		return new String[]{res.substring(0,index).trim(),res.substring(index+1,res.length()).trim()};
	}
	
	public static <K,V> Map<K,V> makeMap(Class K,Class V,Object[]... objs){
		TreeMap<K,V> map=new TreeMap<K,V>();
		for(Object[] o:objs){
			map.put((K)o[0],(V)o[1]);
		}
		return map;
	}
	
	public static String toVString(boolean b){
		return b?"1":"0";
	}
	
	public static boolean toBool(String s){
		switch(s){
			case "1":
			case "true":
				return true;
			default:return false;
		}
	}
	
	public static int toInt(String s){
		if(s==null||s.length()==0)return 0;
		return Integer.parseInt(s);
	}
	
	public static long toLong(String s){
		return Long.parseLong(s);
	}
	
	public static float toFloat(String s){
		return Float.parseFloat(s);
	}
	
	public static double toDouble(String s){
		return Double.parseDouble(s);
	}
}
