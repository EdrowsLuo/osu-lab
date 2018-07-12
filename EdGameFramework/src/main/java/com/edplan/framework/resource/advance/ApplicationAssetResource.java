package com.edplan.framework.resource.advance;
import com.edplan.framework.resource.AssetResource;
import android.content.res.AssetManager;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.resource.DefR;

public class ApplicationAssetResource extends AssetResource
{
	private AResource shaderResource;
	
	private AResource textureResource;
	
	public ApplicationAssetResource(AssetManager m){
		super(m);
		shaderResource=this.subResource(DefR.shaders);
		textureResource=this.subResource(DefR.textures);
	}

	public AResource getShaderResource() {
		return shaderResource;
	}
	
	public AResource getTextureResource() {
		return textureResource;
	}
}
