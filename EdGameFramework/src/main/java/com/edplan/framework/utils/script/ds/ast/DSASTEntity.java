package com.edplan.framework.utils.script.ds.ast;

public interface DSASTEntity {

    default boolean isConstValue() {
        return false;
    }

}
