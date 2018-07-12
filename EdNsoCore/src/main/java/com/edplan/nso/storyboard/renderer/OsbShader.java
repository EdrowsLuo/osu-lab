package com.edplan.nso.storyboard.renderer;
import com.edplan.framework.utils.StringUtil;
import com.edplan.framework.graphics.opengl.shader.advance.BaseShader;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib.Type;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;

public class OsbShader extends BaseShader
{
	public static final String VertexShader;
	
	public static final String FragmentShader;
	
	static{
		VertexShader=StringUtil.link(StringUtil.LINE_BREAK,
									 "uniform mat4 u_MVPMatrix;",
									 "uniform float u_Time;",
									 
									 "attribute vec2 a_TextureCoord;",
									 
									 "attribute vec2 a_AnchorOffset;",

									 //vec4第一位表示起始值，第二位表示结束值，第三位表示开始时间，第四位表示动画长度
									 "attribute vec4 a_PositionX;",
									 "attribute vec4 a_PositionY;",
									 "attribute vec4 a_ScaleX;",
									 "attribute vec4 a_ScaleY;",
									 "attribute vec4 a_Rotation;",
									 "attribute vec4 a_Alpha;",
									 
									 "attribute vec4 a_VaryingColor0;",
									 "attribute vec4 a_VaryingColor1;",
									 "attribute vec2 a_ColorTime;",
									 
									 "attribute float a_XEasing;",
									 "attribute float a_YEasing;",
									 "attribute float a_XSEasing;",
									 "attribute float a_YSEasing;",
									 "attribute float a_REasing;",
									 "attribute float a_CEasing;",
									 "attribute float a_AEasing;",

									 "varying vec2 f_TextureCoord;",
									 "varying vec4 f_VaryingColor;",

									 "@include <Easing>",
									 "@include <Color>",
									 "@include <Vec2>",
									 
									 "float getProgress(float startTime,float duration){",
									 "	if(startTime<u_Time){",
									 "		return 0.0;",
									 "	}else if(duration==0.0){",
									 "		return 1.0;",
									 "	}else return (startTime-u_Time)/duration;",
									 "}",
									 
									 "float makeFloat(vec4 d,int e){",
									 "	return applyEasing(d.r,d.g,getProgress(d.b,d.a),e);",
									 "}",
									 
									 "vec4 makeColor(){",
									 "	return applyEasing(applyAlpha(a_VaryingColor0),applyAlpha(a_VaryingColor1),getProgress(a_ColorTime.x,a_ColorTime.y),te(a_CEasing));",
									 "}",
									 
									 "void main(){",
									 "	f_TextureCoord=a_TextureCoord;",
									 "	f_VaryingColor=makeColor()*makeFloat(a_Alpha,te(a_AEasing));",
									 "  vec2 pos=vec2(makeFloat(a_PositionX,te(a_XEasing)),makeFloat(a_PositionY,te(a_YEasing)));",
									 "	vec2 scale=vec2(makeFloat(a_ScaleX,te(a_XSEasing)),makeFloat(a_ScaleY,te(a_YSEasing)));",
									 "  pos+=rotate(a_AnchorOffset*scale,makeFloat(a_Rotation,te(a_REasing)));",
									 "	gl_Position=u_MVPMatrix*vec4(pos,0.0,1.0);",
									 "}"
									 );
		FragmentShader=StringUtil.link(StringUtil.LINE_BREAK,
									   "precision mediump float;",

									   "varying vec2 f_TextureCoord;",
									   "varying vec4 f_VaryingColor;",

									   "uniform sampler2D u_Texture;",

									   "void main(){",
									   "	vec4 c=texture2D(u_Texture,f_TextureCoord)*f_VaryingColor;",
									   "	if(c.a<0.001)discard;",
									   "	gl_FragColor=c;",
									   "}"
									   );
	}
	
	@PointerName
	public UniformMat4
		uMVPMatrix;
	
	@PointerName
	public UniformFloat
		uTime;
	
	@PointerName
	public UniformSample2D
		uTexture;
	
	@PointerName
	@AttribType(Type.VEC2)
	public VertexAttrib
		aTextureCoord,
		aColorTime,
		aAnchorOffset;
	
	@PointerName
	@AttribType(Type.VEC4)
	public VertexAttrib
		aPositionX,
		aPositionY,
		aScaleX,
		aScaleY,
		aRotation,
		aAlpha,
			
		aVaryingColor0,
		aVaryingColor1;
	
	@PointerName
	@AttribType(Type.INT)
	public VertexAttrib
		aXEasing,
		aYEasing,
		aXSEasing,
		aYSEasing,
		aREasing,
		aCEasing,
		aAEasing;
	
	public OsbShader(){
		super(GLProgram.createProgram(VertexShader,FragmentShader),true);
	}
	
	public void bindTexture(GLTexture t){
		uTexture.loadData(t);
	}
	
	public void loadCamera(Camera c){
		uMVPMatrix.loadData(c.getFinalMatrix());
	}
	
}
