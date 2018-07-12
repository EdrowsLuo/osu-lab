package com.edplan.framework.graphics.opengl;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec4;

public class GLPaint
{
	//#Base Data，每一种绘制都会使用的
	public Color4 mixColor=Color4.rgba(1,1,1,1);
	
	public Color4 varyingColor=Color4.rgba(1,1,1,1);
	
	public float finalAlpha=1;
	//#end Base Data
	
	//#Rect Data，绘制材质矩形时的参数
	public Vec4 padding=new Vec4(0,0,0,0);
	
	public float roundedRadius=0;
	
	public float glowFactor=0.5f;
	
	public Color4 glowColor=Color4.rgba(1, 1, 1, 0.2f);
	//#end Rect Data
	
	//#Line Data，绘制线时的各种参数
	public float strokeWidth=1f;
	//#end Line Data
	
	public GLPaint(){
		
	}
	
	public GLPaint(GLPaint res){
		set(res);
	}
	
	public void set(GLPaint p){
		setMixColor(p.getMixColor());
		setVaryingColor(p.getVaryingColor());
		//setColorMixRate(p.getColorMixRate());
		setFinalAlpha(p.getFinalAlpha());
		setPadding(p.getPadding());
		setRoundedRadius(p.getRoundedRadius());
		setGlowFactor(p.getGlowFactor());
		setGlowColor(p.getGlowColor());
		setStrokeWidth(p.getStrokeWidth());
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth=strokeWidth;
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}
	
	public void setRoundedRadius(float roundedRadius) {
		this.roundedRadius=roundedRadius;
	}

	public float getRoundedRadius() {
		return roundedRadius;
	}

	public void setGlowFactor(float glowFactor) {
		this.glowFactor=glowFactor;
	}

	public float getGlowFactor() {
		return glowFactor;
	}

	public void setGlowColor(Color4 glowColor) {
		this.glowColor.set(glowColor);
	}

	public Color4 getGlowColor() {
		return glowColor;
	}

	public void setPadding(float padding) {
		this.padding.set(padding,padding,padding,padding);
	}
	
	public void setPadding(Vec4 padding){
		this.padding.set(padding);
	}

	public Vec4 getPadding() {
		return padding;
	}

	public void setMixColor(Color4 mixColor) {
		this.mixColor.set(mixColor);
	}

	public Color4 getMixColor() {
		return mixColor;
	}

	public void setVaryingColor(Color4 varyingColor) {
		this.varyingColor.set(varyingColor);
	}

	public Color4 getVaryingColor() {
		return varyingColor;
	}
/*
	public void setColorMixRate(float colorMixRate) {
		this.colorMixRate=colorMixRate;
	}

	public float getColorMixRate() {
		return colorMixRate;
	}
*/
	public void setFinalAlpha(float finalAlpha) {
		this.finalAlpha=finalAlpha;
	}

	public float getFinalAlpha() {
		return finalAlpha;
	}
}
