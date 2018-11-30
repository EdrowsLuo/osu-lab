package com.edplan.framework.timing;

public abstract class Loopable {

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Flag getFlag() {
        return flag;
    }

    public enum Flag {
        Run, Skip, Stop
    }

    private Flag flag = Flag.Run;

    private ILooper looper;

    public void setLooper(ILooper lp) {
        this.looper = lp;
    }

    public ILooper getLooper() {
        return looper;
    }

    public void onRemove() {

    }

    public abstract void onLoop(double deltaTime);
}
