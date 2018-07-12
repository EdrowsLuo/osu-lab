package com.edplan.nso.filepart;
import com.edplan.nso.OsuFilePart;
import java.util.HashMap;

public class PartVariables implements OsuFilePart
{
	public static final String TAG="Variables";
	
	public HashMap<String,String> variables=new HashMap<String,String>();
	
	public void addVariable(String key,String value){
		variables.put(key,value);
	}
	
	public String getVariable(String key){
		return variables.get(key);
	}
	
	@Override
	public String getTag() {

		return TAG;
	}

	@Override
	public String makeString() {

		return "{@Variables}";
	}
}
