package com.edlplan.framework.utils.script.ds.ast.expression;

public class DSASTBoolean implements DSASTValue {

    public static final DSASTBoolean TRUE = new DSASTBoolean(true);

    public static final DSASTBoolean FALSE = new DSASTBoolean(false);

    public static final String S_TRUE = "true", S_FALSE = "false";

    private boolean value;

    private DSASTBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public static DSASTBoolean of(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return value ? S_TRUE : S_FALSE;
    }
}
