package com.edplan.framework.game.g2d.input.touch;

import com.edplan.framework.math.Vec2;
import com.edplan.framework.utils.DirectArray;

public interface IPointer {

    int getPointerID();

    boolean isDown();

    Vec2 getCurrentPosition();




}
