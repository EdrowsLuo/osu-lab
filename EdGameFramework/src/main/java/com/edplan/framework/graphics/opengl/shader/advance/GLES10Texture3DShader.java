package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.shader.GLProgram;

public class GLES10Texture3DShader extends Texture3DShader {
    public GLES10Texture3DShader() {
        super(GLProgram.invalidProgram());
        if (GLWrapped.GL_VERSION != 1) {
            throw new GLException("you can only use GLES10Shader in GL_VERSION=1");
        }
    }

	/*
	@Override
	public void loadPosition(Vec3Buffer buffer) {

		//super.loadPosition(buffer);
		GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
		GLES10.glVertexPointer(buffer.getFloatSize(),GLES10.GL_FLOAT,0,buffer.makeBuffer());
	}

	@Override
	public void loadTexture(AbstractTexture texture) {

		//super.loadTexture(texture);
		GLTexture t=texture.getTexture();
		GLES10.glActiveTexture(GLES10.GL_TEXTURE0);
		GLES10.glBindTexture(GLES10.GL_TEXTURE_2D,t.getTextureId());
		GLES10.glEnable(GLES10.GL_TEXTURE_2D);
	}

	@Override
	public void loadColor(Color4Buffer buffer) {

		//super.loadColor(buffer);
		//Log.v("gl-10","loadColor");
		GLES10.glEnableClientState(GLES10.GL_COLOR_ARRAY);
		GLES10.glColorPointer(4,GLES10.GL_FLOAT,0,buffer.makeBuffer());
		//GLWrapped.checkGlError("loadColor");
	}

	@Override
	protected void loadMVPMatrix(Mat4 mvp) {

		//super.loadMVPMatrix(mvp);
		MLog.test.vOnce("es10loadMVP","es-10-err","you call loadMVPMatrix in GLES10??");
	}

	@Override
	public void loadPaint(GLPaint paint,float alphaAdjust) {

		//super.loadPaint(paint, alphaAdjust);
	}

	@Override
	public void loadMatrix(Camera c) {

		//super.loadMatrix(mvp, mask);
		GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
		GLES10.glLoadIdentity();
		GLES10.glMultMatrixf(c.getMaskMatrix().data,0);
		GLES10.glMatrixMode(GLES10.GL_PROJECTION);
		GLES10.glLoadIdentity();
		GLES10.glMultMatrixf(c.getProjectionMatrix().data,0);
	}

	@Override
	public void loadTexturePosition(Vec2Buffer buffer) {

		//super.loadTexturePosition(buffer);
		//if(true)return;
		GLES10.glEnableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
		GLES10.glTexCoordPointer(2,GLES10.GL_FIXED,0,buffer.makeBuffer());
	}

	@Override
	public boolean loadBatch(BaseBatch batch) {

		return super.loadBatch(batch);
	}

	@Override
	public void loadMixColor(Color4 c) {

		//super.loadMixColor(c);
		//will do nothing
	}

	@Override
	public void loadAlpha(float a) {

		//super.loadAlpha(a);
		//will do nothing
	}

	@Override
	public void applyToGL(int mode,int offset,int count) {

		//super.applyToGL(mode, offset, count);
		GLES10.glDrawArrays(mode,offset,count);
	}

	@Override
	protected void loadMaskMatrix(Mat4 mpm) {

		//super.loadMaskMatrix(mpm);
		MLog.test.vOnce("es10loadMask","es-10-err","you call loadMaskMatrix in GLES10??");
	}
	*/
}
