package com.edlplan.framework.ui.animation;

import com.edlplan.framework.utils.interfaces.Function;

public enum LoopType {
    None(t -> false),
    Loop(t -> false),
    LoopAndReverse(t -> t % 2 == 1),
    Endless(t -> false);

    private final Function<Integer, Boolean> reverseRef;

    LoopType(Function<Integer, Boolean> reverseRef) {
        this.reverseRef = reverseRef;
    }

    public boolean isReverse(int loopCount) {
        return reverseRef.reflect(loopCount);
    }
}
