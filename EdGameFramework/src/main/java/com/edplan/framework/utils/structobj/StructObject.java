package com.edplan.framework.utils.structobj;
import org.json.JSONObject;
import org.json.JSONException;

public interface StructObject
{
	public JSONObject asJson();
	
	public void injectJson(JSONObject data) throws JSONException;
}
