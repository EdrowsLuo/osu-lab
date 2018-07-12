package com.edplan.framework.ui.drawable.sprite;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edplan.framework.utils.StringUtil;
import java.nio.Buffer;
import com.edplan.framework.graphics.opengl.objs.GLTexture;

public class TextureSpriteShader extends SpriteShader
{
	public static final String VERTEX_SHADER,FRAGMENT_SHADER;

	private static TextureSpriteShader instance;

	public static TextureSpriteShader get(){
		if(instance==null)instance=new TextureSpriteShader(VERTEX_SHADER,FRAGMENT_SHADER);
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
											"void main(){",
											"    vec4 c=f_Color*getTextureColor();",
											"    @include <discard>",
											"    gl_FragColor=c;",
											"}"
										});
	}
	
	@PointerName
	public UniformSample2D uTexture;
	
	@PointerName
	@AttribType(VertexAttrib.Type.VEC2)
	public VertexAttrib aTextureCoord;
	
	public TextureSpriteShader(String vs,String fs){
		super(vs,fs);
	}
	
	public void loadTexture(AbstractTexture texture){
		if(texture==null)texture=GLTexture.ErrorTexture;
		uTexture.loadData(texture.getTexture());
	}
	
	public void loadTextureCoord(Buffer b){
		aTextureCoord.loadData(b);
	}
}
