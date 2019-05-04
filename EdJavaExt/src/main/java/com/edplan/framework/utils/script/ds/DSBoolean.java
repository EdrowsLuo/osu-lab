package com.edplan.framework.utils.script.ds;

public class DSBoolean extends DSValue {

    public static final String TYPE_NAME = "b";

    public boolean value;

    public DSBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String typeName() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

}
