package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.math.FMath;

public class HitArea {

    public PositionChecker circle(float ox, float oy, float radius) {
        final float sqRadius = radius * 2;
        return (x, y) -> FMath.squareDistance(ox - x, oy - y) <= sqRadius;
    }

}
