package com.edlplan.framework.utils.script.ds.ast.struct;

import com.edlplan.framework.utils.script.ds.ast.DSASTEntity;

/**
 * name<string> => xxxxx<any>,
 */
public class DSASTNameValuePair implements DSASTStruct{

    private String name;

    private DSASTEntity value;

    public DSASTNameValuePair(String name, DSASTEntity value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DSASTEntity getValue() {
        return value;
    }

    public void setValue(DSASTEntity value) {
        this.value = value;
    }
}
