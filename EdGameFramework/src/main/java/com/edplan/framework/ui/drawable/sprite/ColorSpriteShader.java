package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.utils.StringUtil;

public class ColorSpriteShader extends SpriteShader
{
	public static final String VERTEX_SHADER,FRAGMENT_SHADER;

	static{
		VERTEX_SHADER=StringUtil.link(StringUtil.LINE_BREAK,new String[]{
										  "@include <SpriteBase.vs>",
										  "void main(){ setUpSpriteBase(); }"
									  });
		FRAGMENT_SHADER=StringUtil.link(StringUtil.LINE_BREAK,new String[]{
											"precision highp float;",
											"@include <SpriteBase.fs>",
											"void main(){",
											//"    @include <discard>",
											"    gl_FragColor=f_Color;",
											"}"
										});
	}
	
	public static ColorSpriteShader instance;
	
	public static ColorSpriteShader get(){
		if(instance==null)instance=new ColorSpriteShader();
		return instance;
	}
	
	public ColorSpriteShader(){
		super(VERTEX_SHADER,FRAGMENT_SHADER);
	}
}
