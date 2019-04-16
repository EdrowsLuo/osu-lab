package com.edplan.framework.graphics.opengl.shader.advance;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.ShaderGlobals;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.utils.Lazy;

import java.nio.FloatBuffer;

public class ColorShader extends BaseShader{

    public static final Lazy<ColorShader> DEFAULT;

    public static final Lazy<ColorShader> ALPHA; //only render alpha channel

    static {
        DEFAULT = Lazy.create(() -> new ColorShader(
                GLProgram.createProgram(
                        "" +
                                "uniform mat4 u_MVPMatrix;\n" +
                                "uniform mat4 u_MaskMatrix;\n" +
                                "uniform float u_FinalAlpha;\n" +
                                "uniform vec4 u_AccentColor;\n" +
                                "\n" +
                                "attribute vec3 a_Position;\n" +
                                "attribute vec4 a_VaryingColor;\n" +
                                "\n" +
                                "varying vec4 f_VaryingColor;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    f_VaryingColor = u_AccentColor * a_VaryingColor * u_FinalAlpha;\n" +
                                "    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);\n" +
                                "}",
                        "" +
                                "precision mediump float;\n" +
                                "\n" +
                                "varying lowp vec4 f_VaryingColor;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    gl_FragColor = f_VaryingColor;\n" +
                                "}"
                )
        ));
        ALPHA = Lazy.create(() -> new ColorShader(
                GLProgram.createProgram(
                        "" +
                                "uniform mat4 u_MVPMatrix;\n" +
                                "uniform mat4 u_MaskMatrix;\n" +
                                "uniform float u_FinalAlpha;\n" +
                                "uniform vec4 u_AccentColor;\n" +
                                "\n" +
                                "attribute vec3 a_Position;\n" +
                                "attribute vec4 a_VaryingColor;\n" +
                                "\n" +
                                "varying float f_VaryingColor;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    f_VaryingColor = u_AccentColor.a * a_VaryingColor.a * u_FinalAlpha;\n" +
                                "    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);\n" +
                                "}",
                        "" +
                                "precision mediump float;\n" +
                                "\n" +
                                "varying lowp float f_VaryingColor;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    gl_FragColor = vec4(f_VaryingColor);\n" +
                                "}"
                )
        ));
    }

    @BaseShader.PointerName
    public UniformMat4 uMVPMatrix;

    @BaseShader.PointerName
    public UniformMat4 uMaskMatrix;

    @BaseShader.PointerName
    public UniformFloat uFinalAlpha;

    @BaseShader.PointerName
    public UniformColor4 uAccentColor;

    @BaseShader.PointerName
    @BaseShader.AttribType(VertexAttrib.Type.VEC3)
    public VertexAttrib aPosition;

    @BaseShader.PointerName
    @BaseShader.AttribType(VertexAttrib.Type.VEC4)
    public VertexAttrib aVaryingColor;

    public static final int STEP = (3 + 1) * 4;

    protected ColorShader(GLProgram program) {
        super(program, true);
    }

    public void loadMatrix(Camera c) {
        loadMVPMatrix(c.getFinalMatrix());
        loadMaskMatrix(c.getMaskMatrix());
    }

    protected void loadMVPMatrix(Mat4 mvp) {
        uMVPMatrix.loadData(mvp);
    }

    protected void loadMaskMatrix(Mat4 mpm) {
        uMaskMatrix.loadData(mpm);
    }

    public void loadBuffer(FloatBuffer buffer) {
        int pos = buffer.position();
        int limit = buffer.limit();
        aPosition.loadData(buffer, 3, GLES20.GL_FLOAT, STEP, false);
        buffer.position(pos + 3);
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
