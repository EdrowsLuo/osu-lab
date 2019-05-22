package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edlplan.framework.graphics.opengl.shader.advance.BaseShader;
import com.edlplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edlplan.framework.utils.StringUtil;

import java.nio.Buffer;

/**
 * 舍弃功能达到较快的绘制速度
 * 对alpha的设置被无效了，请通过AccentColor处理alpha
 */
public class FastTextureSpriteShader extends TextureSpriteShader{
    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static FastTextureSpriteShader instance;

    public static FastTextureSpriteShader get() {
        if (instance == null) instance = new FastTextureSpriteShader(VERTEX_SHADER, FRAGMENT_SHADER);
        return instance;
    }

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK,
                "uniform mat4 u_MVPMatrix;",
                "attribute vec3 a_Position;",
                "attribute vec2 a_TextureCoord;",
                "varying vec2 f_TextureCoord;",
                "void main(){",
                "    f_TextureCoord=a_TextureCoord;",
                "    gl_Position=u_MVPMatrix*vec4(a_Position,1.0);",
                "}");
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK,
                "precision mediump float;",
                "varying vec2 f_TextureCoord;",
                "uniform sampler2D u_Texture;",
                "uniform float u_Alpha;",
                "void main(){",
                "    gl_FragColor=u_Alpha*texture2D(u_Texture,f_TextureCoord);",
                "}");
    }

    @BaseShader.PointerName
    public UniformSample2D uTexture;

    @BaseShader.PointerName
    @BaseShader.AttribType(VertexAttrib.Type.VEC2)
    public VertexAttrib aTextureCoord;

    public FastTextureSpriteShader(String vs, String fs) {
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
