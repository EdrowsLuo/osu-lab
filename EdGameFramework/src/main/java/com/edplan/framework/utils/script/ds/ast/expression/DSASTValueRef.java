package com.edplan.framework.utils.script.ds.ast.expression;

import com.edplan.framework.utils.script.ds.ast.DSASTEntity;

public class DSASTValueRef implements DSASTEntity {

    private String name;

    public DSASTValueRef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
