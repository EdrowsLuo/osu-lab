package com.edplan.osulab.ui.config;
import com.edplan.framework.config.ConfigList;

public class DebugConfig extends ConfigList
{
	private static DebugConfig instance;
	
	static{
		instance=new DebugConfig();
	}
	
	public DebugConfig(){
		setListName("Debug");
		putBoolean("Show FPS",true);
		putBoolean("Boolean Property1",false);
	}
	
	public static DebugConfig get(){
		return instance;
	}
}
