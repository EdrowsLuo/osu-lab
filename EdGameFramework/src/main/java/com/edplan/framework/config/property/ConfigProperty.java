package com.edplan.framework.config.property;
import com.edplan.framework.utils.structobj.StructObject;
import org.json.JSONObject;
import java.util.HashMap;

public abstract class ConfigProperty implements StructObject
{
	public static final String TYPE_NULL="null";
	
	public abstract String propertyType();
	
	private static HashMap<String,Loader> loaders=new HashMap<String,Loader>();
	
	public static void registerLoader(String type,Loader l){
		loaders.put(type,l);
	}
	
	public static ConfigProperty loadProperty(String type,JSONObject data){
		Loader l=loaders.get(type);
		if(l==null){
			throw new IllegalArgumentException();
		}
		return l.load(data);
	}
	
	public interface Loader{
		public ConfigProperty load(JSONObject obj);
	}
}
