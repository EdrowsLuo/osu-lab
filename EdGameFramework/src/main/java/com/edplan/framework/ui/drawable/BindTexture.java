package com.edplan.framework.ui.drawable;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public class BindTexture
{
	private GLTexture texture;
	
	//这里是1x1坐标里的坐标
	private Vec2 org=new Vec2(0.5f,0.5f);
	
	private Vec2 scale=new Vec2(1,1);
	
	private float rotation=0;
	
	private float alpha=1;
	
	/*
	private 对色调的调整
	*/

	public BindTexture(GLTexture texture){
		this.texture=texture;
	}
	
	public int getWidth(){
		return texture.getWidth();
	}
	
	public int getHeight(){
		return texture.getHeight();
	}

	public void setTexture(GLTexture texture) {
		this.texture=texture;
	}

	public GLTexture getTexture() {
		return texture;
	}

	public void setOrg(Vec2 org) {
		this.org.set(org);
	}

	public Vec2 getOrg() {
		return org;
	}

	public void setScale(Vec2 scale) {
		this.scale.set(scale);
	}

	public Vec2 getScale() {
		return scale;
	}

	public void setRotation(float rotation) {
		this.rotation=rotation;
	}

	public float getRotation() {
		return rotation;
	}

	public void setAlpha(float alpha) {
		this.alpha=alpha;
	}

	public float getAlpha() {
		return alpha;
	}
	
}
