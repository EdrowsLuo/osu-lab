package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.math.Vec2;

@FunctionalInterface
public interface PositionChecker {
    boolean checkPosition(float x, float y);

    default boolean checkPosition(Vec2 v) {
        return checkPosition(v.x, v.y);
    }
}
