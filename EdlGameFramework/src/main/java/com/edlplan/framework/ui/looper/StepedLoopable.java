package com.edlplan.framework.ui.looper;

import com.edlplan.framework.timing.Loopable;

public abstract class StepedLoopable extends Loopable {
    public abstract int getStep();
}
