package com.edlplan.framework.utils.script.ds.ast.expression;

import com.edlplan.framework.utils.script.ds.ast.DSASTEntity;

public class DSASTVec2 implements DSASTValue {

    private DSASTEntity arg1, arg2;

    public DSASTVec2(DSASTEntity arg1, DSASTEntity arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public DSASTEntity getArg1() {
        return arg1;
    }

    public DSASTEntity getArg2() {
        return arg2;
    }

    public void setArg1(DSASTEntity arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(DSASTEntity arg2) {
        this.arg2 = arg2;
    }

    @Override
    public boolean isConstValue() {
        return arg1.isConstValue() && arg2.isConstValue();
    }

    @Override
    public String toString() {
        return '(' + arg1.toString() + ", " + arg2.toString() + ')';
    }
}
