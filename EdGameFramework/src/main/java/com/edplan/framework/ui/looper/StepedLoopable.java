package com.edplan.framework.ui.looper;

import com.edplan.framework.timing.Loopable;

public abstract class StepedLoopable extends Loopable {
    public abstract int getStep();
}
