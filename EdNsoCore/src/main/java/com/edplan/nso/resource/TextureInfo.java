package com.edplan.nso.resource;
import com.edplan.framework.graphics.opengl.objs.GLTexture;

public class TextureInfo extends ResourceInfo<GLTexture>
{
	public TextureInfo(String path){
		super(path);
	}
	
	public TextureInfo setTexture(GLTexture t){
		setRes(t);
		return this;
	}
}
