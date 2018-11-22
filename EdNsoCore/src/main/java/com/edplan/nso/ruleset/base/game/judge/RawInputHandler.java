package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.ui.inputs.EdKeyEvent;
import com.edplan.framework.ui.inputs.EdMotionEvent;

public interface RawInputHandler {

    boolean onMotionEvent(EdMotionEvent event);

    boolean onKeyEvent(EdKeyEvent event);

}
