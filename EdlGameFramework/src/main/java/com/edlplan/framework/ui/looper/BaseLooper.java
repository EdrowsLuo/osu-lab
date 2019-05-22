package com.edlplan.framework.ui.looper;

import com.edlplan.framework.timing.ILooper;
import com.edlplan.framework.timing.Loopable;
import com.edlplan.framework.timing.MTimer;
import com.edlplan.framework.utils.SafeList;

import java.util.Iterator;

public class BaseLooper<T extends Loopable> implements ILooper<T> {
    private SafeList<T> loopables;

    private MTimer timer;

    public BaseLooper() {
        loopables = new SafeList<T>();
        timer = new MTimer();
    }

    public double getDeltaTime() {
        return timer.getDeltaTime();
    }

    public void setTimer(MTimer timer) {
        this.timer = timer;
    }

    public MTimer getTimer() {
        return timer;
    }

    @Override
    public void prepare() {

        getTimer().initial();
    }

    @Override
    public void addLoopable(T l) {

        l.setLooper(this);
        loopables.add(l);
    }

    protected void runLoopable(T l) {
        l.onLoop(timer.getDeltaTime());
    }

    protected void onRemoveLoopable(T l) {

    }

    @Override
    public void loop(double deltaTime) {
        timer.refresh(deltaTime);

        loopables.startIterate();
        Iterator<T> iter = loopables.iterator();
        T l;
        while (iter.hasNext()) {
            l = iter.next();
            switch (l.getFlag()) {
                case Run:
                    runLoopable(l);
                    break;
                case Skip:
                    break;
                case Stop:
                    onRemoveLoopable(l);
                    iter.remove();
                    break;
                default:
                    throw new NullPointerException("Loopable.getFlag() can't return null");
            }
        }
        loopables.endIterate();
    }
}
