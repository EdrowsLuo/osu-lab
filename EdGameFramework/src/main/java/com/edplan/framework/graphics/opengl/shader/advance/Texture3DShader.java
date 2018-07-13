package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.base.IHasTexturePosition;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.shader.Attr;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.Unif;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edplan.framework.math.Mat4;

import java.nio.FloatBuffer;

public class Texture3DShader extends ColorShader {
    public static Texture3DShader Invalid = new InvalidTexture3DShader();

    @PointerName(Unif.Texture)
    public UniformSample2D uTexture;

    @PointerName(Attr.Texturesition)
    @AttribType(VertexAttrib.Type.VEC2)
    public VertexAttrib vTexturePosition;

    protected Texture3DShader(GLProgram program) {
        super(program, true);
    }

    protected Texture3DShader(GLProgram p, boolean i) {
        super(p, i);
    }

    @Override
    public boolean loadBatch(BaseBatch batch) {

        if (super.loadBatch(batch)) {
            if (batch instanceof IHasTexturePosition) {
                loadTexturePosition(((IHasTexturePosition) batch).makeTexturePositionBuffer());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void loadTexture(AbstractTexture texture) {
        uTexture.loadData(texture.getTexture());
    }

    public void loadTexturePosition(FloatBuffer buffer) {
        vTexturePosition.loadData(buffer);
    }

    public static final Texture3DShader createT3S(String vs, String fs) {
        Texture3DShader s = new Texture3DShader(GLProgram.createProgram(vs, fs));
        return s;
    }

    public static class InvalidTexture3DShader extends Texture3DShader {
        public InvalidTexture3DShader() {
            super(GLProgram.invalidProgram(), false);
        }

        @Override
        public void loadPosition(FloatBuffer buffer) {

            //super.loadPosition(buffer);
        }

        @Override
        public void loadTexture(AbstractTexture texture) {

            //super.loadTexture(texture);
        }

        @Override
        public void loadColor(FloatBuffer buffer) {

            //super.loadColor(buffer);
        }

        @Override
        protected void loadMVPMatrix(Mat4 mvp) {

            //super.loadMVPMatrix(mvp);
        }

        @Override
        public void loadPaint(GLPaint paint, float alphaAdjust) {

            //super.loadPaint(paint, alphaAdjust);
        }

        @Override
        public void loadMatrix(Camera c) {

            //super.loadMatrix(mvp, mask);
        }

        @Override
        public void loadTexturePosition(FloatBuffer buffer) {

            //super.loadTexturePosition(buffer);
        }

        @Override
        public boolean loadBatch(BaseBatch batch) {

            return true;//super.loadBatch(batch);
        }

        @Override
        public void loadMixColor(Color4 c) {

            //super.loadMixColor(c);
        }

        @Override
        public void loadAlpha(float a) {

            //super.loadAlpha(a);
        }

        @Override
        protected void loadMaskMatrix(Mat4 mpm) {

            //super.loadMaskMatrix(mpm);
        }

        @Override
        public void applyToGL(int mode, int offset, int count) {

            //super.applyToGL(mode, offset, count);
        }


    }
}
