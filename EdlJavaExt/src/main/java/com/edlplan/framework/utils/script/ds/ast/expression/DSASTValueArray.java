package com.edlplan.framework.utils.script.ds.ast.expression;

import com.edlplan.framework.utils.script.ds.ast.DSASTEntity;

import java.util.List;

public class DSASTValueArray implements DSASTValue {

    private List<DSASTEntity> values;

    public DSASTValueArray(List<DSASTEntity> values) {
        this.values = values;
    }

    public void setValues(List<DSASTEntity> values) {
        this.values = values;
    }

    public List<DSASTEntity> getValues() {
        return values;
    }

}
