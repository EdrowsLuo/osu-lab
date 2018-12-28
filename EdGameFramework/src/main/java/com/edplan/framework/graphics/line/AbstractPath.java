package com.edplan.framework.graphics.line;

import com.edplan.framework.math.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface AbstractPath {
    Vec2 get(int idx);

    int size();

    float getWidth();

    default LinePath fitToLinePath() {
        List<Vec2> list = new ArrayList<>();
        int size = size();
        Vec2 pre = null;
        for (int i = 0; i < size; i++) {
            Vec2 cur = new Vec2(get(i));
            if (pre == null || Vec2.length(pre, cur) > 0.01) {
                list.add(pre = cur);
            }
        }
        return new LinePath(list, getWidth());
    }
}
