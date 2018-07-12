package com.edplan.framework.ui.drawable.sprite;
import com.edplan.framework.graphics.opengl.shader.Attr;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.advance.BaseShader;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.objs.Color4;
import java.nio.Buffer;

public class SpriteShader extends BaseShader
{
	@PointerName
	public UniformMat4 uMVPMatrix;

	@PointerName
	public UniformFloat uAlpha;
	
	@PointerName
	public UniformColor4 uAccentColor;
	
	@PointerName
	@AttribType(VertexAttrib.Type.VEC3)
	public VertexAttrib aPosition;

	@PointerName
	@AttribType(VertexAttrib.Type.VEC2)
	public VertexAttrib aSpritePosition;

	@PointerName
	@AttribType(VertexAttrib.Type.VEC4)
	public VertexAttrib aColor;
	
	public SpriteShader(String vs,String fs){
		super(GLProgram.createProgram(vs,fs),true);
	}
	
	public void loadCamera(Camera c){
		uMVPMatrix.loadData(c.getFinalMatrix());
	}
	
	public void loadAlpha(float a){
		uAlpha.loadData(a);
	}
	
	public void loadAccentColor(Color4 c){
		uAccentColor.loadData(c);
	}
	
	public void loadPositionBuffer(Buffer b){
		aPosition.loadData(b);
	}
	
	public void loadSpritePositionBuffer(Buffer b){
		aSpritePosition.loadData(b);
	}
	
	public void loadColor(Buffer b){
		aColor.loadData(b);
	}
}
