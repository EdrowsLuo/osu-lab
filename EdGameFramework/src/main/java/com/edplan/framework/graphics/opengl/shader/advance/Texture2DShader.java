package com.edplan.framework.graphics.opengl.shader.advance;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.ShaderGlobals;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.utils.Lazy;

import java.nio.FloatBuffer;

public class Texture2DShader extends BaseShader {

    public static final Lazy<Texture2DShader> DEFAULT;

    static {
        DEFAULT = Lazy.create(() -> new Texture2DShader(
                GLProgram.createProgram(
                        "" +
                                "uniform mat4 u_MVPMatrix;\n" +
                                "uniform mat4 u_MaskMatrix;\n" +
                                "uniform float u_FinalAlpha;\n" +
                                "uniform vec4 u_AccentColor;\n" +
                                "\n" +
                                "attribute vec4 a_PositionAndCoord;\n" +
                                "attribute vec4 a_VaryingColor;\n" +
                                "\n" +
                                "varying vec4 f_VaryingColor;\n" +
                                "varying vec2 f_TextureCoord;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    f_TextureCoord = a_PositionAndCoord.ba;\n" +
                                "    f_VaryingColor = a_VaryingColor * u_FinalAlpha;\n" +
                                "    gl_Position = u_MVPMatrix * vec4(a_PositionAndCoord.rg, 1.0, 1.0);\n" +
                                "}",
                        "" +
                                "precision mediump float;\n" +
                                "\n" +
                                "uniform sampler2D u_Texture;\n" +
                                "varying lowp vec4 f_VaryingColor;\n" +
                                "varying vec2 f_TextureCoord;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    gl_FragColor = f_VaryingColor * texture2D(u_Texture, f_TextureCoord);\n" +
                                "}"
                )
        ));
    }

    @PointerName
    public UniformMat4 uMVPMatrix;

    @PointerName
    public UniformMat4 uMaskMatrix;

    @PointerName
    public UniformFloat uFinalAlpha;

    @PointerName
    public UniformColor4 uAccentColor;

    @PointerName
    @AttribType(VertexAttrib.Type.VEC4)
    public VertexAttrib aPositionAndCoord;

    @PointerName
    @AttribType(VertexAttrib.Type.VEC4)
    public VertexAttrib aVaryingColor;

    @PointerName
    public UniformSample2D uTexture;

    public static final int STEP = (4 + 1) * 4;

    protected Texture2DShader(GLProgram program) {
        super(program, true);
    }

    protected Texture2DShader(GLProgram p, boolean i) {
        super(p, i);
    }

    public void loadTexture(AbstractTexture texture) {
        uTexture.loadData(texture.getTexture());
    }

    public void loadMatrix(Camera c) {
        loadMVPMatrix(c.getFinalMatrix());
        loadMaskMatrix(c.getMaskMatrix());
    }

    public void loadAccentColor(Color4 c) {
        uAccentColor.loadData(c);
    }

    protected void loadMVPMatrix(Mat4 mvp) {
        uMVPMatrix.loadData(mvp);
    }

    protected void loadMaskMatrix(Mat4 mpm) {
        uMaskMatrix.loadData(mpm);
    }

    public void loadAlpha(float a) {
        uFinalAlpha.loadData(a);
    }

    public void loadBuffer(FloatBuffer buffer) {
        int pos = buffer.position();
        int limit = buffer.limit();
        aPositionAndCoord.loadData(buffer, 4, GLES20.GL_FLOAT, STEP, false);
        buffer.position(pos + 4);
        aVaryingColor.loadData(buffer, 4, GLES20.GL_UNSIGNED_BYTE, STEP, true);
        buffer.position(pos);
        buffer.limit(limit);
    }

    @Override
    public void loadShaderGlobals(ShaderGlobals globals) {
        uFinalAlpha.loadData(globals.alpha);
        uAccentColor.loadData(globals.accentColor);
        loadMatrix(globals.camera);
    }
}
