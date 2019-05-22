package com.edlplan.framework.utils.script.ds;

public class DSVec2 extends DSValue {

    public static final String TYPE_NAME = "v";

    public float x, y;

    public DSVec2() {

    }

    public DSVec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String typeName() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
