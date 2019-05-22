package com.edlplan.framework.async;

public class IntProgressHolder extends ProgressHolder{

    private int max;

    private int prog;

    public void setMax(int max) {
        this.max = max;
    }

    public void setIntProgress(int prog) {
        this.prog = prog;
    }

    @Override
    public double getProgress() {
        return prog / (double) max;
    }
}
