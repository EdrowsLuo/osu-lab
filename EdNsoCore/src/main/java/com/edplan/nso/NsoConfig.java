package com.edplan.nso;
import com.edplan.framework.config.ConfigList;
import com.edplan.framework.config.property.ConfigBoolean;
import com.edplan.framework.config.ConfigEntry;

public class NsoConfig extends ConfigList
{
	public ConfigEntry hasUI;
	
	public NsoConfig(){
		hasUI=putBoolean("hasUI",true);
	}
}
