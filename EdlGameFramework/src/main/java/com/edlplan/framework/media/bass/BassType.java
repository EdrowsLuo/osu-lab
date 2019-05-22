package com.edlplan.framework.media.bass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public abstract @interface BassType {
    public BassChannel.Type type();
}
