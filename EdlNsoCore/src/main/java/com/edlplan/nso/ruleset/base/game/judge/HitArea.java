package com.edlplan.nso.ruleset.base.game.judge;

import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.Vec2;

public class HitArea {

    public static PositionChecker circle(float ox, float oy, float radius) {
        final float sqRadius = radius * radius;
        return (x, y) -> FMath.squareDistance(ox - x, oy - y) <= sqRadius;
    }

    public static PositionChecker circle(Vec2 ref, float radius) {
        final float sqRadius = radius * radius;
        return (x, y) -> FMath.squareDistance(ref.x - x, ref.y - y) <= sqRadius;
    }

}
