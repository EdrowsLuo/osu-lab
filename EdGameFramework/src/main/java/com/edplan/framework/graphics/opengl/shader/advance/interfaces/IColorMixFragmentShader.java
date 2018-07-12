package com.edplan.framework.graphics.opengl.shader.advance.interfaces;
import com.edplan.framework.graphics.opengl.objs.Color4;

public interface IColorMixFragmentShader
{
	public void setMixColor(Color4 color);
	
	public void setMixFactor(float f);
}
