package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformVec2;
import com.edplan.framework.utils.StringUtil;

public class TextureCircleSprite extends BaseRectTextureSprite<TextureCircleShader>
{
	private float innerRadius;
	private float radius;

	public TextureCircleSprite(MContext c){
		super(c);
	}
	
	public void resetRadius(){
		setInnerRadius(0);
		setRadius(0);
	}

	public void setInnerRadius(float innerRadius){
		this.innerRadius=innerRadius;
	}

	public float getInnerRadius(){
		return innerRadius;
	}

	public void setRadius(float radius){
		this.radius=radius;
	}

	public float getRadius(){
		return radius;
	}

	@Override
	protected TextureCircleShader createShader(){

		return TextureCircleShader.get();
	}

	@Override
	protected void prepareShader(BaseCanvas canvas){

		super.prepareShader(canvas);
		shader.loadCircleData(innerRadius,radius);
	}
}

class TextureCircleShader extends TextureSpriteShader{

	public static final String VERTEX_SHADER,FRAGMENT_SHADER;

	private static TextureCircleShader instance;

	public static TextureCircleShader get(){
		if(instance==null)instance=new TextureCircleShader();
		return instance;
	}

	static{
		VERTEX_SHADER=StringUtil.link(StringUtil.LINE_BREAK,new String[]{
										  "@include <TextureSpriteBase.vs>",
										  "void main(){ setUpSpriteBase(); }"
									  });
		FRAGMENT_SHADER=StringUtil.link(StringUtil.LINE_BREAK,new String[]{
											"precision highp float;",
											
											"@include <TextureSpriteBase.fs>",
										    "uniform vec2 u_Radius;",
											"void main(){",
											"    float v=length(f_Position);",
											"    float a=min(",
											"        smoothstep(u_Radius.x-1.0,u_Radius.x,v),",
											"        1.0-smoothstep(u_Radius.y-1.0,u_Radius.y,v));",
											"    vec4 c=f_Color*getTextureColor()*a;",
											"    @include <discard>",
											"    gl_FragColor=c;",
											"}"
										});
	}


	@PointerName
	public UniformVec2 uRadius;

	public TextureCircleShader(){
		super(VERTEX_SHADER,FRAGMENT_SHADER);
	}

	public void loadCircleData(float innerRadius,float radius){
		uRadius.loadData(innerRadius,radius);
	}
}
