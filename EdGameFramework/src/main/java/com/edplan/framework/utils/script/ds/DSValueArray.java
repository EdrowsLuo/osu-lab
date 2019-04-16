package com.edplan.framework.utils.script.ds;

public class DSValueArray extends DSValue {

    private String typeName;

    public DSValue[] values;

    public DSValueArray(DSValue[] values) {
        this.values = values;
    }

    @Override
    public String typeName() {
        if (typeName == null) {
            typeName = "l" + values[0].typeName();
        }
        return typeName;
    }

}
