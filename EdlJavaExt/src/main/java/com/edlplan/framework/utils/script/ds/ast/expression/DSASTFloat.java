package com.edlplan.framework.utils.script.ds.ast.expression;

public class DSASTFloat implements DSASTValue {

    private float value;

    public DSASTFloat(float f) {
        this.value = f;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean isConstValue() {
        return true;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }
}
