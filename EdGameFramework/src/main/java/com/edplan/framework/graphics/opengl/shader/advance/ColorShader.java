package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.base.IHasColor;
import com.edplan.framework.graphics.opengl.batch.base.IHasPosition;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.ShaderGlobals;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.utils.Lazy;

import java.nio.FloatBuffer;

public class ColorShader extends BaseShader {
    public static ColorShader Invalid = new InvalidColorShader();

    public static final Lazy<ColorShader> DEFAULT;

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
                                "    f_VaryingColor=a_VaryingColor*u_FinalAlpha;\n" +
                                "    gl_Position=u_MVPMatrix*vec4(a_Position,1.0);\n" +
                                "}",
                        "" +
                                "precision mediump float;\n" +
                                "\n" +
                                "varying lowp vec4 f_VaryingColor;\n" +
                                "\n" +
                                "void main(){\n" +
                                "    gl_FragColor=f_VaryingColor;\n" +
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
    @AttribType(VertexAttrib.Type.VEC4)
    public VertexAttrib aVaryingColor;

    protected ColorShader(GLProgram program) {
        super(program, true);
    }

    protected ColorShader(GLProgram p, boolean i) {
        super(p, i);
    }

    public void loadPaint(GLPaint paint, float alphaAdjust) {
        loadAccentColor(paint.getAccentColor());
        loadAlpha(paint.getFinalAlpha() * alphaAdjust);
    }

    public boolean loadBatch(BaseBatch batch) {
        if (batch instanceof IHasColor && batch instanceof IHasPosition) {
            loadColor(((IHasColor) batch).makeColorBuffer());
            loadPosition(((IHasPosition) batch).makePositionBuffer());
            return true;
        } else {
            return false;
        }
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

    public void loadPosition(FloatBuffer buffer) {
        aPosition.loadData(buffer);
    }

    public void loadColor(FloatBuffer buffer) {
        aVaryingColor.loadData(buffer);
    }

    public void loadAlpha(float a) {
        uFinalAlpha.loadData(a);
    }

    public void applyToGL(int mode, int offset, int count) {
        GLWrapped.drawArrays(mode, offset, count);
    }

    @Override
    public void loadShaderGlobals(ShaderGlobals globals) {
        uFinalAlpha.loadData(globals.alpha);
        uAccentColor.loadData(globals.accentColor);
        loadMatrix(globals.camera);
    }

    public static final ColorShader createCS(String vs, String fs) {
        ColorShader s = new ColorShader(GLProgram.createProgram(vs, fs));
        return s;
    }

    public static class GL10ColorShader extends ColorShader {
        public GL10ColorShader() {
            super(GLProgram.invalidProgram());
        }
    }

    public static class InvalidColorShader extends ColorShader {
        public InvalidColorShader() {
            super(GLProgram.invalidProgram(), false);
        }

        @Override
        public void loadColor(FloatBuffer buffer) {

            //super.loadColor(buffer);
        }

        @Override
        public void loadPaint(GLPaint paint, float alphaAdjust) {

            //super.loadPaint(paint, alphaAdjust);
        }

        @Override
        protected void loadMVPMatrix(Mat4 mvp) {

            //super.loadMVPMatrix(mvp);
        }

        @Override
        protected void loadMaskMatrix(Mat4 mpm) {

            //super.loadMaskMatrix(mpm);
        }

        @Override
        public boolean loadBatch(BaseBatch batch) {

            return true;//super.loadBatch(batch);
        }

        @Override
        public void loadPosition(FloatBuffer buffer) {

            //super.loadPosition(buffer);
        }

        @Override
        public void loadMatrix(Camera c) {

            //super.loadMatrix(mvp, mask);
        }

        @Override
        public void loadAlpha(float a) {

            //super.loadAlpha(a);
        }

        @Override
        public void applyToGL(int mode, int offset, int count) {

            //super.applyToGL(mode, offset, count);
        }
    }
}
