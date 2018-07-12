package com.edplan.framework.ui.text.font;
import java.util.HashMap;

public class TextureCharacterPool
{
	private HashMap<Character,CharacterInfo> infoMap=new HashMap<Character,CharacterInfo>();
	
	private float height;
	
	public void put(CharacterInfo info){
		infoMap.put(info.getText(),info);
	}
	
	public CharacterInfo get(char c){
		return infoMap.get(c);
	}
	
	public int size(){
		return infoMap.size();
	}
	
	public boolean has(char c){
		return infoMap.containsKey(c);
	}
}
