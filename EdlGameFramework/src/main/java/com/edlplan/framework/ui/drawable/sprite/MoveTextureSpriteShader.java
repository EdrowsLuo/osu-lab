package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edlplan.framework.utils.StringUtil;

public class MoveTextureSpriteShader extends TextureSpriteShader {

    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static MoveTextureSpriteShader instance;

    public static MoveTextureSpriteShader get() {
        if (instance == null) instance = new MoveTextureSpriteShader(VERTEX_SHADER, FRAGMENT_SHADER);
        return instance;
    }

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK,
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
                "void main(){ setUpSpriteBase(); }");
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK,
                "precision mediump float;",
                "varying lowp vec4 f_Color;",
                "varying vec2 f_TextureCoord;",
                "uniform sampler2D u_Texture;",
                "uniform sampler2D u_Backbuffer;",
                "void main(){",
                "    gl_FragColor = f_Color * texture2D(u_Backbuffer, gl_Position.xy) * 2.0;",
                "}");
    }

    @PointerName
    @SampleIndex(1)
    public UniformSample2D uBackbuffer;

    public MoveTextureSpriteShader(String vs, String fs) {
        super(vs, fs);
    }
}
