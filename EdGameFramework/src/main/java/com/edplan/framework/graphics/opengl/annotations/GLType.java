package com.edplan.framework.graphics.opengl.annotations;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface GLType
{
	public enum Type{SAMPLE2D,FLOAT,VEC2,VEC3,VEC4,MAT2,MAT3,MAT4}
	
	Type value() default Type.FLOAT;
}
