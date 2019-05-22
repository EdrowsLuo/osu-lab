package com.edlplan.framework.utils.script.ds.ast;

public interface DSASTEntity {

    default boolean isConstValue() {
        return false;
    }

}
