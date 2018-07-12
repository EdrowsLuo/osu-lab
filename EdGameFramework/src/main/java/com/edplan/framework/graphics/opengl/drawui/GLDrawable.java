package com.edplan.framework.graphics.opengl.drawui;
import com.edplan.framework.graphics.opengl.GLCanvas2D;


/**
 *一个抽象类，描述如何在一个Layer上进行绘制
 */
public abstract class GLDrawable
{
	
	
	public abstract void prepareForDraw();
	
	public abstract void render(GLCanvas2D canvas);
}
