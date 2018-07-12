package com.edplan.framework.ui.text.font;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.texture.TextureRegion;
import com.edplan.framework.math.RectI;

public class CharacterInfo
{
	private char text;
	
	private AbstractTexture texture;

	public CharacterInfo(char text,AbstractTexture texture){
		this.text=text;
		this.texture=texture;
	}
	
	public CharacterInfo(char text,GLTexture texture,int x,int y,int width,int height){
		this(text,new TextureRegion(texture,new RectF(x,y,width,height)));
	}

	public int getWidth(){
		return texture.getWidth();
	}
	
	public int getHeight(){
		return texture.getHeight();
	}

	public void setText(char text) {
		this.text=text;
	}

	public char getText() {
		return text;
	}

	public void setTexture(AbstractTexture texture) {
		this.texture=texture;
	}

	public AbstractTexture getTexture() {
		return texture;
	}
}
