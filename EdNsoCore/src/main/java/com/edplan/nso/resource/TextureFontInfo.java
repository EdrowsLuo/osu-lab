package com.edplan.nso.resource;
import com.edplan.framework.ui.text.font.TextureCharacterPool;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.text.font.CharacterInfo;

public class TextureFontInfo extends ResourceInfo
{
	private TextureCharacterPool pool=new TextureCharacterPool();
	
	public TextureFontInfo(String path){
		super(path);
	}

	public TextureCharacterPool getPool() {
		return pool;
	}
	
	public void addToPool(char c,AbstractTexture texture){
		pool.put(new CharacterInfo(c,texture));
	}
}
