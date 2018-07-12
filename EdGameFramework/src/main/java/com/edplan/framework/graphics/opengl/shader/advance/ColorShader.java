package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.base.IHasColor;
import com.edplan.framework.graphics.opengl.batch.base.IHasPosition;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.shader.Attr;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.Unif;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.math.Mat4;
import java.nio.FloatBuffer;
import android.util.Log;

public class ColorShader extends BaseShader
{
	public static ColorShader Invalid=new InvalidColorShader();

	@PointerName
	public UniformMat4 uMVPMatrix;
	
	@PointerName
	public UniformMat4 uMaskMatrix;
	
	@PointerName
	public UniformFloat uFinalAlpha;
	
	@PointerName
	public UniformColor4 uMixColor;
	
	@PointerName
	@AttribType(VertexAttrib.Type.VEC3)
	public VertexAttrib aPosition;
	
	@PointerName(Attr.Color)
	@AttribType(VertexAttrib.Type.VEC4)
	public VertexAttrib vColor;
	
	protected ColorShader(GLProgram program){
		super(program,true);
	}
	
	protected ColorShader(GLProgram p,boolean i){
		super(p,i);
	}
	
	public void loadPaint(GLPaint paint,float alphaAdjust){
		loadMixColor(paint.getMixColor());
		loadAlpha(paint.getFinalAlpha()*alphaAdjust);
	}

	public boolean loadBatch(BaseBatch batch){
		if(batch instanceof IHasColor&&batch instanceof IHasPosition){
			loadColor(((IHasColor)batch).makeColorBuffer());
			loadPosition(((IHasPosition)batch).makePositionBuffer());
			return true;
		}else{
			return false;
		}
	}
	
	public void loadMatrix(Camera c){
		loadMVPMatrix(c.getFinalMatrix());
		loadMaskMatrix(c.getMaskMatrix());
	}
	
	public void loadMixColor(Color4 c){
		uMixColor.loadData(c);
	}

	protected void loadMVPMatrix(Mat4 mvp){
		uMVPMatrix.loadData(mvp);
	}
	
	protected void loadMaskMatrix(Mat4 mpm){
		uMaskMatrix.loadData(mpm);
	}
	
	public void loadPosition(FloatBuffer buffer){
		aPosition.loadData(buffer);
	}
	
	public void loadColor(FloatBuffer buffer){
		vColor.loadData(buffer);
	}

	public void loadAlpha(float a){
		uFinalAlpha.loadData(a);
	}
	
	public void applyToGL(int mode,int offset,int count){
		GLWrapped.drawArrays(mode,offset,count);
	}
	
	public static final ColorShader createCS(String vs,String fs){
		ColorShader s=new ColorShader(GLProgram.createProgram(vs,fs));
		return s;
	}
	
	public static class GL10ColorShader extends ColorShader{
		public GL10ColorShader(){
			super(GLProgram.invalidProgram());
		}
	}
	
	public static class InvalidColorShader extends ColorShader{
		public InvalidColorShader(){
			super(GLProgram.invalidProgram(),false);
		}

		@Override
		public void loadColor(FloatBuffer buffer) {

			//super.loadColor(buffer);
		}

		@Override
		public void loadPaint(GLPaint paint,float alphaAdjust) {

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
		public void loadMixColor(Color4 c) {

			//super.loadMixColor(c);
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
		public void applyToGL(int mode,int offset,int count) {

			//super.applyToGL(mode, offset, count);
		}
	}
}
