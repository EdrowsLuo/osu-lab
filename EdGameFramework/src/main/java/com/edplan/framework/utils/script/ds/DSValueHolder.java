package com.edplan.framework.utils.script.ds;

public class DSValueHolder {

    private DSValue value;

    private DSExpression expression;

    public DSValueHolder(DSExpression expression) {
        this.value = expression.getValue();
        this.expression = expression;
    }

    public void update() {
        expression.update();
    }

    public DSValue getValue() {
        return value;
    }

    public DSFloat getFloat() {
        return (DSFloat) value;
    }

    public DSVec2 getVec2() {
        return (DSVec2) value;
    }

}
