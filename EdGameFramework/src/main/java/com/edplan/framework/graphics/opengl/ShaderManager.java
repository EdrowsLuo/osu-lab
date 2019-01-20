package com.edplan.framework.graphics.opengl;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edplan.framework.graphics.opengl.shader.advance.LegacyTexture3DShader;
import com.edplan.framework.graphics.opengl.shader.compile.CompileRawStringStore;
import com.edplan.framework.resource.AResource;

import java.io.IOException;

public class ShaderManager {
    public static class PATH {
        private static final String PATH_Texture3DShader = "StdTexture3DShader";
        private static final String PATH_ColorShader = "StdColorShader";
    }

    private static ShaderManager shaderManager;

    private LegacyTexture3DShader legacyTexture3DShader;

    private ColorShader colorShader;

    private AResource res;

    private ShaderManager() {

    }

    public ShaderManager(ShaderManager s) {
        set(s);
    }

    public ShaderManager(AResource _res) {
        init(_res);
    }

    public void init(AResource _res) {
        res = _res;
        loadShader();
    }

    public void set(ShaderManager s) {
        legacyTexture3DShader = s.legacyTexture3DShader;
        colorShader = s.colorShader;
        res = s.res;
    }

    private void loadShader() {
        if (GLWrapped.GL_VERSION >= 2)
            try {
                legacyTexture3DShader =
                        LegacyTexture3DShader.createT3S(
                                res.loadText(PATH.PATH_Texture3DShader + ".vs"),
                                res.loadText(PATH.PATH_Texture3DShader + ".fs")
                        );
                colorShader =
                        ColorShader.createCS(
                                res.loadText(PATH.PATH_ColorShader + ".vs"),
                                res.loadText(PATH.PATH_ColorShader + ".fs")
                        );
            } catch (IOException e) {
                e.printStackTrace();
                throw new GLException("err load shader from asset! msg: " + e.getMessage(), e);
            }
    }

    public ColorShader getColorShader() {
        return colorShader;
    }

    public void setLegacyTexture3DShader(LegacyTexture3DShader legacyTexture3DShader) {
        this.legacyTexture3DShader = legacyTexture3DShader;
    }

    public LegacyTexture3DShader getLegacyTexture3DShader() {
        return legacyTexture3DShader;
    }

    public static void initStatic(MContext context) {
        if (GLWrapped.GL_VERSION >= 2) {
            final AResource shaderDir = context.getAssetResource().subResource("shaders");
            final AResource storeDir = shaderDir.subResource("store");

            try {
                for (String name : storeDir.list("")) {
                    if (name.endsWith(".store")) {
                        CompileRawStringStore.get().addToStore(storeDir.loadText(name));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            shaderManager = new ShaderManager(shaderDir);
        } else {

        }
    }

    public static ShaderManager getNewDefault() {
        if (GLWrapped.GL_VERSION >= 2) {
            return new ShaderManager(shaderManager);
        } else {
            return null;
        }
    }
}
