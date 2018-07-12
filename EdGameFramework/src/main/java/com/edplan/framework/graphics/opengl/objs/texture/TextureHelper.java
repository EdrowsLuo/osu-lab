package com.edplan.framework.graphics.opengl.objs.texture;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectI;
import com.edplan.framework.math.RectF;

public class TextureHelper
{
	public static TextureRegion[][] split(GLTexture texture,int row,int cro){
		TextureRegion[][] rgs=new TextureRegion[row][cro];
		float deltaWidth=texture.getWidth()/row;
		float deltaHeight=texture.getHeight()/cro;
		for(int x=0;x<row;x++){
			for(int y=0;y<cro;y++){
				rgs[x][y]=new TextureRegion(texture,new RectF(deltaWidth*x,deltaHeight*y,deltaWidth,deltaHeight));
			}
		}
		return rgs;
	}
}
