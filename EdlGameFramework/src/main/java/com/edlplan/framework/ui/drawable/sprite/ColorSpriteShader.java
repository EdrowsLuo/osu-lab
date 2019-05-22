package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.utils.StringUtil;

public class ColorSpriteShader extends SpriteShader {
    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "uniform mat4 u_MVPMatrix;",
                "uniform vec4 u_AccentColor;",
                "uniform float u_Alpha;",
                "attribute vec3 a_Position;",
                "attribute vec4 a_Color;",
                "varying vec4 f_Color;",
                "void main(){",
                "    f_Color=a_Color*u_AccentColor*u_Alpha;",
                "    gl_Position=u_MVPMatrix*vec4(a_Position,1.0);",
                "}"
        });
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "precision mediump float;",
                "varying vec4 f_Color;",
                "void main(){",
                "    gl_FragColor=f_Color;",
                "}"
        });
    }

    public static ColorSpriteShader instance;

    public static ColorSpriteShader get() {
        if (instance == null) instance = new ColorSpriteShader();
        return instance;
    }

    public ColorSpriteShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }
}
