package com.edplan.framework.timing;

public interface IRunnableHandler {
    void post(Runnable r);

    void post(Runnable r, double delayMS);
}
