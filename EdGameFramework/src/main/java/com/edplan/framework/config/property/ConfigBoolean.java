package com.edplan.framework.config.property;
import org.json.JSONObject;
import org.json.JSONException;

public class ConfigBoolean extends ConfigProperty
{
	public static final String TYPE="boolean";
	
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
	
	private boolean data;
	
	public boolean get(){
		return data;
	}
	
	public void set(boolean b){
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

		set(data.optBoolean("data"));
	}

}
