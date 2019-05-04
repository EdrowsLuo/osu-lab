package com.edplan.framework.utils.script.ds;

import java.util.HashMap;

public class DSOverrideContext implements IDSContext {

    private IDSContext parent;

    private HashMap<String, DSValue> values = new HashMap<>();

    private HashMap<String, DSFunction> functions = new HashMap<>();

    public DSOverrideContext() {
        parent = DSDefaultContext.getDefault();
    }

    public void addValue(String name, DSValue value) throws DSException {
        values.put(name, value);
    }

    public void addFunction(String name, DSFunction function) throws DSException {
        functions.put(name, function);
    }

    @Override
    public DSValue getValue(String name) {
        DSValue value = values.get(name);
        if (value == null) {
            return parent.getValue(name);
        }
        return value;
    }

    @Override
    public DSFunction getFunction(String name) {
        DSFunction value = functions.get(name);
        if (value == null) {
            return parent.getFunction(name);
        }
        return value;
    }

}
