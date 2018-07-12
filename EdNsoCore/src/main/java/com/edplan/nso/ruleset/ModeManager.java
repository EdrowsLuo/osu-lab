package com.edplan.nso.ruleset;
import java.util.Map;
import java.util.TreeMap;

public class ModeManager
{
	public static final int MODE_STD=0;
	public static final int MODE_TAIKO=1;
	public static final int MODE_CTB=2;
	public static final int MODE_MANIA=3;
	
	private Map<Class,Integer> modeID=new TreeMap<Class,Integer>();
	
	
	
	public int getModeId(Class c){
		return modeID.get(c);
	}
}
