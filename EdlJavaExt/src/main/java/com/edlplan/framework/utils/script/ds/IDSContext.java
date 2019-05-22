package com.edlplan.framework.utils.script.ds;

public interface IDSContext {

    DSValue getValue(String name);

    DSFunction getFunction(String name);

}
