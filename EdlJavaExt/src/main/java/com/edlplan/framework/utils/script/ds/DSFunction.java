package com.edlplan.framework.utils.script.ds;

public interface DSFunction {

    void invoke(DSValue ret,DSValue[] args);

    int argsCount();

    char retType();
}
