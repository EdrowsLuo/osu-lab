package com.edlplan.framework.utils.script.ds.ast;

import java.util.List;

public class DSAST {

    private List<DSASTEntity> entitys;

    public List<DSASTEntity> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<DSASTEntity> entitys) {
        this.entitys = entitys;
    }

    @Override
    public String toString() {
        return entitys.get(0).toString();
    }
}
