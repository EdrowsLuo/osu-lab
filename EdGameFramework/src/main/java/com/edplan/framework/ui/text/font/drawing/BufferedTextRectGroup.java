package com.edplan.framework.ui.text.font.drawing;
import com.edplan.framework.graphics.opengl.objs.advanced.Texture3DRect;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLPaint;

public interface BufferedTextRectGroup
{
	public void add(int page,Texture3DRect rect);
	
	public void post(GLCanvas2D canvas,GLPaint paint);
}
