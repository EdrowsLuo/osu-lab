package com.edplan.framework.graphics.opengl.shader;
import com.edplan.framework.graphics.opengl.annotations.GLType;
import com.edplan.framework.graphics.opengl.annotations.GLType.Type;

public final class Unif{
	public static final @GLType(Type.MAT4)     String MVPMatrix="u_MVPMatrix";
	public static final @GLType(Type.MAT4)     String MaskMatrix="u_MaskMatrix";
	public static final @GLType(Type.SAMPLE2D) String Texture="u_Texture";
	public static final @GLType(Type.FLOAT)    String FinalAlpha="u_FinalAlpha";
	public static final @GLType(Type.FLOAT)    String ColorMixRate="u_ColorMixRate";
	public static final @GLType(Type.VEC4)     String MixColor="u_MixColor";
	
	
	public static final @GLType(Type.MAT2)     String DrawingRect="u_DrawingRect";
	public static final @GLType(Type.VEC4)    String Padding="u_Padding";
	public static final @GLType(Type.FLOAT)    String RoundedRidaus="u_Ridaus";
	public static final @GLType(Type.FLOAT)    String GlowFactor="u_GlowFactor";
	public static final @GLType(Type.VEC4)     String GlowColor="u_GlowColor";
	public static final @GLType(Type.MAT2)     String RoundedInner="u_RoundedInner";
	public static final @GLType(Type.MAT2)     String InnerRect="u_InnerRect";
	
}
