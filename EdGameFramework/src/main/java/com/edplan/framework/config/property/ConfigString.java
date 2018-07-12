package com.edplan.framework.config.property;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigString extends ConfigProperty
{
	public static final String TYPE="string";

	static{
		ConfigProperty.registerLoader(TYPE,new ConfigProperty.Loader(){
				@Override
				public ConfigProperty load(JSONObject obj){

					ConfigBoolean b=new ConfigBoolean();
					b.injectJson(obj);
					return b;
				}
			});
	}

	private String data;

	public String get(){
		return data;
	}

	public void set(String b){
		this.data=b;
	}

	@Override
	public String propertyType(){

		return TYPE;
	}

	@Override
	public JSONObject asJson(){

		JSONObject obj=new JSONObject();
		try{
			obj.put("data",data);
		}catch(JSONException e){}
		return obj;
	}

	@Override
	public void injectJson(JSONObject data){

		set(data.optString("data"));
	}
}
