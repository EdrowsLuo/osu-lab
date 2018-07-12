package com.edplan.framework.graphics.opengl.shader.uniforms;
import android.opengl.GLES20;
import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.graphics.opengl.shader.DataUniform;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.math.Mat2;
import com.edplan.framework.math.RectF;

public class UniformMat2 extends DataUniform<Mat2>
{
	protected UniformMat2(int h){
		super(h);
	}
	
	@Override
	public void loadData(Mat2 mat){
		if(available)GLES20.glUniformMatrix2fv(getHandle(),1,false,mat.data,0);
	}
	
	public void loadData(RectF rect){
		if(available)GLES20.glUniformMatrix2fv(getHandle(),1,false,new float[]{rect.getX1(),rect.getY1(),rect.getX2(),rect.getY2()},0);
	}
	
	public void loadData(RectF rect,float padding){
		if(available)GLES20.glUniformMatrix2fv(getHandle(),1,false,new float[]{rect.getX1()+padding,rect.getY1()+padding,rect.getX2()-padding,rect.getY2()-padding},0);
	}
	
	public static UniformMat2 findUniform(GLProgram program,String name){
		UniformMat2 um=new UniformMat2(GLES20.glGetUniformLocation(program.getProgramId(),name));
		//if(um.handle==-1)throw new GLException("handle "+name+" NOT found");
		return um;
	}
}
