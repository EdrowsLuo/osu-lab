package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edplan.framework.utils.StringUtil;

import java.nio.Buffer;

public class TextureSpriteShader extends SpriteShader {
    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static TextureSpriteShader instance;

    public static TextureSpriteShader get() {
        if (instance == null) instance = new TextureSpriteShader(VERTEX_SHADER, FRAGMENT_SHADER);
        return instance;
    }

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "uniform mat4 u_MVPMatrix;\n" +
                        "uniform vec4 u_AccentColor;\n" +
                        "uniform float u_Alpha;\n" +
                        "\n" +
                        "attribute vec3 a_Position;\n" +
                        "attribute vec2 a_SpritePosition;\n" +
                        "attribute vec4 a_Color;\n" +
                        "attribute vec2 a_TextureCoord;\n" +
                        "varying vec4 f_Color;\n" +
                        "varying vec2 f_TextureCoord;\n" +
                        "    " +
                        "void setUpSpriteBase(){\n" +
                        "    f_TextureCoord=a_TextureCoord;\n" +
                        "    f_Color=a_Color*u_AccentColor*u_Alpha;\n" +
                        "    gl_Position=u_MVPMatrix*vec4(a_Position,1.0);\n" +
                        "}",
                "void main(){ setUpSpriteBase(); }"
        });
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "precision mediump float;",
                "varying vec4 f_Color;",
                "varying vec2 f_TextureCoord;",
                "uniform sampler2D u_Texture;",
                "void main(){",
                "    gl_FragColor=f_Color*texture2D(u_Texture,f_TextureCoord);",
                "}"
        });
    }

    @PointerName
    public UniformSample2D uTexture;

    @PointerName
    @AttribType(VertexAttrib.Type.VEC2)
    public VertexAttrib aTextureCoord;

    public TextureSpriteShader(String vs, String fs) {
        super(vs, fs);
    }

    public void loadTexture(AbstractTexture texture) {
        if (texture == null) texture = GLTexture.ErrorTexture;
        uTexture.loadData(texture.getTexture());
    }

    public void loadTextureCoord(Buffer b) {
        aTextureCoord.loadData(b);
    }
}
