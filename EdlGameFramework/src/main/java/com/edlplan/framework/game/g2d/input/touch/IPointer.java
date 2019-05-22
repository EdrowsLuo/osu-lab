package com.edlplan.framework.game.g2d.input.touch;

import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.DirectArray;

public interface IPointer {

    int getPointerID();

    boolean isDown();

    Vec2 getCurrentPosition();




}
