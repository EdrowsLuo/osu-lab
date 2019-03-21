package com.edplan.framework.graphics.shape;

import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;

public class PathBuilder {

    private IEditablePath editablePath;

    private Vec2 current = new Vec2();

    public PathBuilder(IEditablePath editablePath) {
        this.editablePath = editablePath;
    }

    public static int suggestedClip(float ang) {
        float abs = Math.abs(ang);
        if (abs <= FMath.PiHalf) {
            return 12;
        } else if (abs <= FMath.Pi) {
            return 24;
        } else {
            return 48;
        }
    }

    public PathBuilder moveTo(float x, float y) {
        current.set(x, y);
        editablePath.addPoint(current.copy());
        return this;
    }

    public PathBuilder circleMove(Vec2 o, float ang) {
        int sugClip = suggestedClip(ang);
        float deltaAng = ang / sugClip;
        for (int i = 0; i < sugClip; i++) {
            current.rotate(o, deltaAng);
            editablePath.addPoint(current.copy());
        }
        return this;
    }


}
