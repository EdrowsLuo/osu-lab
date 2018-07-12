package com.edplan.nso.resource.annotation;
import com.edplan.nso.resource.ResType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AResType
{
	public ResType value() default ResType.BINARY;
}
