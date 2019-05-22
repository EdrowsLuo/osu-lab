package com.edlplan.nso.ruleset.base.game.judge;

import com.edlplan.framework.ui.inputs.EdKeyEvent;
import com.edlplan.framework.ui.inputs.EdMotionEvent;

public interface RawInputHandler {

    boolean onMotionEvent(EdMotionEvent... event);

    boolean onKeyEvent(EdKeyEvent event);

}
