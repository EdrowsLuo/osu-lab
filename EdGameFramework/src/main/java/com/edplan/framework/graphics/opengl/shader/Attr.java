package com.edplan.framework.graphics.opengl.shader;
import com.edplan.framework.graphics.opengl.annotations.GLType;
import com.edplan.framework.graphics.opengl.annotations.GLType.Type;

public final class Attr{
	public static final @GLType(Type.VEC4) String Color="a_VaryingColor";
	public static final @GLType(Type.VEC3) String Position="a_Position";
	public static final @GLType(Type.VEC3) String Normal="a_Normal";
	public static final @GLType(Type.VEC2) String Texturesition="a_TexturePosition";
	public static final @GLType(Type.VEC2) String RectPosition="a_RectPosition";
}
	
