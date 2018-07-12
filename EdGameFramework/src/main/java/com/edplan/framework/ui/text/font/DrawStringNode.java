package com.edplan.framework.ui.text.font;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;

public class DrawStringNode
{
	private CharacterInfo info;
	
	private float drawBaseX;
	
	private float drawWidth;
	
	private float drawHeight;

	public void setDrawHeight(float drawHeight) {
		this.drawHeight=drawHeight;
	}

	public float getDrawHeight() {
		return drawHeight;
	}

	public void setDrawWidth(float drawWidth) {
		this.drawWidth=drawWidth;
	}

	public float getDrawWidth() {
		return drawWidth;
	}

	public char getText() {
		return info.getText();
	}

	public void setInfo(CharacterInfo info) {
		this.info=info;
	}

	public CharacterInfo getInfo() {
		return info;
	}

	public void setDrawBaseX(float drawBaseX) {
		this.drawBaseX=drawBaseX;
	}

	public float getDrawBaseX() {
		return drawBaseX;
	}}
