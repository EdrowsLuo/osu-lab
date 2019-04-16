package com.edplan.framework.utils.script.ds;

public class DSFloat extends DSValue {

    public static final String TYPE_NAME = "f";

    public float value;

    public DSFloat() {

    }

    public DSFloat(float f) {
        this.value = f;
    }

    @Override
    public String typeName() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }
}
