package com.edlplan.framework.resource.advance;

import android.content.res.AssetManager;

import com.edlplan.framework.resource.AResource;
import com.edlplan.framework.resource.AssetResource;
import com.edlplan.framework.resource.DefR;

public class ApplicationAssetResource extends AssetResource {
    private AResource shaderResource;

    private AResource textureResource;

    public ApplicationAssetResource(AssetManager m) {
        super(m);
        shaderResource = this.subResource(DefR.shaders);
        textureResource = this.subResource(DefR.textures);
    }

    public AResource getShaderResource() {
        return shaderResource;
    }

    public AResource getTextureResource() {
        return textureResource;
    }
}
