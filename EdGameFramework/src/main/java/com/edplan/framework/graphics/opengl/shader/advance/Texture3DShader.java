package com.edplan.framework.graphics.opengl.shader.advance;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
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

public class Texture3DShader  extends BaseShader{

    public static final Lazy<Texture3DShader> DEFAULT;

    static {
        DEFAULT = Lazy.create(() -> new Texture3DShader(
                GLProgram.createProgram(
                        "" +
                                "uniform mat4 u_MVPMatrix;\n" +
                                "uniform mat4 u_MaskMatrix;\n" +
                                "uniform float u_FinalAlpha;\n" +
                                "uniform vec4 u_AccentColor;\n" +
                                "\n" +
                                "attribute vec3 a_Position;\n" +
                                "attribute vec2 a_Coord;\n" +
                                "attribute vec4 a_VaryingColor;\n" +
                                "\n" +
                                "varying vec4 f_VaryingColor;\n" +
                                "varying vec2 f_TextureCoord;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    f_TextureCoord = a_Coord;\n" +
                                "    f_VaryingColor = a_VaryingColor * u_FinalAlpha;\n" +
                                "    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);\n" +
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
    @AttribType(VertexAttrib.Type.VEC3)
    public VertexAttrib aPosition;

    @PointerName
    @AttribType(VertexAttrib.Type.VEC2)
    public VertexAttrib aCoord;

    @PointerName
    @AttribType(VertexAttrib.Type.VEC4)
    public VertexAttrib aVaryingColor;

    @PointerName
    public UniformSample2D uTexture;

    public static final int STEP = (3 + 2 + 1) * 4;

    protected Texture3DShader(GLProgram program) {
        super(program, true);
    }

    public void loadTexture(AbstractTexture texture) {
        uTexture.loadData(texture.getTexture());
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
        aCoord.loadData(buffer, 2, GLES20.GL_FLOAT, STEP, false);
        buffer.position(pos + 5);
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

    /**
     * 特殊化，不绘制实质性图像的shader（但是会影响 depth test）
     */
    public static class DepthShader extends Texture3DShader {

        public static final Lazy<DepthShader> DEFAULT;

        static {
            DEFAULT = Lazy.create(() -> new DepthShader(
                    GLProgram.createProgram(
                            "" +
                                    "uniform mat4 u_MVPMatrix;\n" +
                                    "\n" +
                                    "attribute vec3 a_Position;\n" +
                                    "\n" +
                                    "void main(){\n" +
                                    "    gl_Position = u_MVPMatrix * vec4(a_Position.xy,a_Position.z-0.01, 1.0);\n" +
                                    "}",
                            "" +
                                    "precision mediump float;\n" +
                                    "void main(){\n" +
                                    "    gl_FragColor = vec4(0.0);\n" +
                                    "}"
                    )
            ));
        }


        protected DepthShader(GLProgram program) {
            super(program);
        }

        @Override
        public void loadShaderGlobals(ShaderGlobals globals) {
            loadMatrix(globals.camera);
        }

        @Override
        public void loadBuffer(FloatBuffer buffer) {
            int pos = buffer.position();
            int limit = buffer.limit();
            aPosition.loadData(buffer, 3, GLES20.GL_FLOAT, STEP, false);
            buffer.position(pos);
            buffer.limit(limit);
        }
    }
}
