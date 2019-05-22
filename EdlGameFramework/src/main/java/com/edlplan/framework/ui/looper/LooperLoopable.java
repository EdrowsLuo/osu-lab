package com.edlplan.framework.ui.looper;

import com.edlplan.framework.timing.Loopable;

public class LooperLoopable<T extends Loopable> extends Loopable {
    private BaseLooper<T> looper = new BaseLooper<T>();

    public void addLoopable(T l) {
        looper.addLoopable(l);
    }

    @Override
    public void onLoop(double deltaTime) {

        looper.loop(deltaTime);
    }

}
