package com.edplan.framework.graphics.opengl;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Mat4;

public class GL10Canvas2D extends BaseCanvas
{
	private int width;
	private int height;

	public GL10Canvas2D(int width,int height){
		this.width=width;
		this.height=height;
		initial();
	}
	
	@Override
	public boolean isPrepared() {

		return true;
	}

	@Override
	public void prepare() {

	}

	@Override
	public void unprepare() {

	}
	
	@Override
	public int getDefWidth() {

		return width;
	}

	@Override
	public int getDefHeight() {

		return height;
	}

	@Override
	public BlendSetting getBlendSetting() {

		return GLWrapped.blend;
	}

	@Override
	protected void checkCanDraw() {

		if(GLWrapped.GL_VERSION!=1){
			throw new GLException("you can only use GL10Canvas2D in GLES10....please use GLCanvas when it's GLES20 or higher");
		}
	}

	@Override
	public CanvasData getDefData() {

		CanvasData d=new CanvasData();
		d.setCurrentProjMatrix(createDefProjMatrix());
		d.setCurrentMaskMatrix(Mat4.createIdentity());
		d.setHeight(getDefHeight());
		d.setWidth(getDefWidth());
		d.setShaders(ShaderManager.getGL10FakerShader());
		return d;
	}

	@Override
	public void clearBuffer() {

		GLWrapped.clearDepthAndColorBuffer();
	}

	@Override
	public void drawColor(Color4 c) {

		GLWrapped.setClearColor(c);
		GLWrapped.clearColorBuffer();
	}

}
