package com.edplan.framework.graphics.opengl.fast;
import com.edplan.framework.graphics.opengl.shader.advance.BaseShader;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.utils.StringUtil;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import java.nio.Buffer;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class FastShader extends BaseShader
{
	public static final String VertexShader;
	
	public static final String FragmentShader;
	
	public static final int STEP=9*4;
	
	public static final int OFFSET_POSITION=0;
	
	public static final int OFFSET_TEXTURE_COORD=3*4;
	
	public static final int OFFSET_COLOR=5*4;
	
	static{
		VertexShader=StringUtil.link(StringUtil.LINE_BREAK,
		"uniform mat4 u_MVPMatrix;",
		
		"attribute vec3 a_Position;",
		"attribute vec2 a_TextureCoord;",
		"attribute vec4 a_VaryingColor;",
		
		"varying vec2 f_TextureCoord;",
		"varying vec4 f_VaryingColor;",
		
		"void main(){",
		"	f_TextureCoord=a_TextureCoord;",
		"	f_VaryingColor=vec4(a_VaryingColor.rgb*a_VaryingColor.a,a_VaryingColor.a);",
		"	gl_Position=u_MVPMatrix*vec4(a_Position,1.0);",
		"}"
		);
		FragmentShader=StringUtil.link(StringUtil.LINE_BREAK,
		"precision mediump float;",

		"varying vec2 f_TextureCoord;",
		"varying vec4 f_VaryingColor;",

		"uniform sampler2D u_Texture;",

		"void main(){",
		"	vec4 c=texture2D(u_Texture,f_TextureCoord)*f_VaryingColor;",
		"	if(c.a<0.005)discard;",
		"	gl_FragColor=c;",
		"}"
		);
	}
	
	@PointerName
	public UniformMat4 uMVPMatrix;
	
	@PointerName
	@AttribType(VertexAttrib.Type.VEC3)
	@AttribVAOData(step=STEP,offset=OFFSET_POSITION)
	public VertexAttrib aPosition;
	
	@PointerName
	@AttribType(VertexAttrib.Type.VEC2)
	@AttribVAOData(step=STEP,offset=OFFSET_TEXTURE_COORD)
	public VertexAttrib aTextureCoord;
	
	@PointerName
	@AttribType(VertexAttrib.Type.VEC4)
	@AttribVAOData(step=STEP,offset=OFFSET_COLOR)
	public VertexAttrib aVaryingColor;
	
	@PointerName
	public UniformSample2D uTexture;
	
	public FastShader(){
		super(makeProgram(),true);
	}
	
	public void bindTexture(AbstractTexture t){
		uTexture.loadData(t.getTexture());
	}
	
	public void loadCamera(Camera c){
		uMVPMatrix.loadData(c.getFinalMatrix());
	}
	
	public void loadAttibutes(Buffer position,Buffer textureCoord,Buffer color){
		aPosition.loadData(position,Vec3.FLOATS*4);
		aTextureCoord.loadData(textureCoord,Vec2.FLOATS*4);
		aVaryingColor.loadData(color,Color4.FLOATS*4);
	}
	
	public void bindAttributes(Buffer b){
		int pos=b.position();
		b.position(pos+aPosition.offset);
		aPosition.loadData(b);
		b.position(pos+aTextureCoord.offset);
		aTextureCoord.loadData(b);
		b.position(pos+aVaryingColor.offset);
		aVaryingColor.loadData(b);
	}
	
	public void bindAttributes(){
		aPosition.loadVAOData();
		aTextureCoord.loadVAOData();
		aVaryingColor.loadVAOData();
	}
	
	private static GLProgram makeProgram(){
		return GLProgram.createProgram(VertexShader,FragmentShader);
	}
}
