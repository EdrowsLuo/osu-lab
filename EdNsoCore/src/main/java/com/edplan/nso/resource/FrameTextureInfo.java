package com.edplan.nso.resource;
import com.edplan.framework.graphics.opengl.objs.texture.FrameTexture;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public class FrameTextureInfo extends ResourceInfo<FrameTexture>
{
	public FrameTextureInfo(String path){
		super(path);
		setRes(new FrameTexture());
	}
	
	public void addFrame(AbstractTexture texture){
		getRes().addFrame(texture);
	}
	
	public FrameTexture copyTexture(){
		return getRes().sharedInstance();
	}
}
