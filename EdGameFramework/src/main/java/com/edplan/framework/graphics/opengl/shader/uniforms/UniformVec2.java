package com.edplan.framework.graphics.opengl.shader.uniforms;

import android.opengl.GLES20;
import com.edplan.framework.graphics.opengl.shader.DataUniform;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.math.Vec2;

public class UniformVec2 extends DataUniform<Vec2>
{
	public static float SCALE=1000;
	
	private UniformVec2(int h){
		super(h);
	}
	
	public void loadData(float x,float y){
		if(available)GLES20.glUniform2f(getHandle(),x,y);
	}

	@Override
	public void loadData(Vec2 t){
		loadData(t.x,t.y);
	}

	public static UniformVec2 findUniform(GLProgram program,String name){
		UniformVec2 um=new UniformVec2(GLES20.glGetUniformLocation(program.getProgramId(),name));
		//if(um.handle==-1)throw new GLException("handle "+name+" NOT found");
		return um;
	}
}
