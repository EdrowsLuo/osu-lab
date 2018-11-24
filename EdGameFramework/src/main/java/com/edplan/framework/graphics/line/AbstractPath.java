package com.edplan.framework.graphics.line;

import com.edplan.framework.math.Vec2;

public interface AbstractPath {
    Vec2 get(int idx);

    int size();

    float getWidth();
}
