package com.edplan.framework.ui.text.font.drawing;

import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import java.util.ArrayList;
import com.edplan.framework.ui.text.font.bmfont.BMFont;

public class BufferedTextVertex 
{
	private ArrayList<Texture3DBatch> batchs;
	
	private BMFont font;
	
	public BufferedTextVertex(BMFont font){
		this.font=font;
	}
}
