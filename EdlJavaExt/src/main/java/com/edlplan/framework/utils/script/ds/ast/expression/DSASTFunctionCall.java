package com.edlplan.framework.utils.script.ds.ast.expression;

import com.edlplan.framework.utils.script.ds.ast.DSASTEntity;

import java.util.List;

public class DSASTFunctionCall implements DSASTExpression {

    private String name;

    private List<DSASTEntity> args;

    public DSASTFunctionCall(String name, List<DSASTEntity> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DSASTEntity> getArgs() {
        return args;
    }

    public void setArgs(List<DSASTEntity> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('#').append(name).append('(');
        if (args.size() == 0) {
            sb.append(')');
            return sb.toString();
        } else {
            sb.append(args.get(0));
        }
        for (int i = 1; i < args.size(); i++) {
            sb.append(", ").append(args.get(i));
        }
        sb.append(')');
        return sb.toString();
    }
}
