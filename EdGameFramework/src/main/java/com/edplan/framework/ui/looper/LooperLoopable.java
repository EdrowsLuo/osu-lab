package com.edplan.framework.ui.looper;

import com.edplan.superutils.interfaces.Loopable;

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
