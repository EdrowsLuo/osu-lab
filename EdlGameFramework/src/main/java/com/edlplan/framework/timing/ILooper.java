package com.edlplan.framework.timing;

public interface ILooper<T extends Loopable> {
    void loop(double deltaTime);

    void prepare();

    void addLoopable(T l);
}
