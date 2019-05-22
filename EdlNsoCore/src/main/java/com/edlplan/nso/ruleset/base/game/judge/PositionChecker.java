package com.edlplan.nso.ruleset.base.game.judge;

import com.edlplan.framework.math.Vec2;

@FunctionalInterface
public interface PositionChecker {
    boolean checkPosition(float x, float y);

    default boolean checkPosition(Vec2 v) {
        return checkPosition(v.x, v.y);
    }
}
