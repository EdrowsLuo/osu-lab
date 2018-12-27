package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.math.FMath;

public class HitArea {

    public static PositionChecker circle(float ox, float oy, float radius) {
        final float sqRadius = radius * radius;
        return (x, y) -> FMath.squareDistance(ox - x, oy - y) <= sqRadius;
    }

}