package com.edplan.nso.ruleset.std.beatmap;

import com.edplan.nso.ruleset.base.beatmap.Beatmap;
import com.edplan.nso.ruleset.base.beatmap.controlpoint.ControlPoints;

public class StdBeatmap extends Beatmap {

    private ControlPoints controlPoints;

    public ControlPoints getControlPoints() {
        if (controlPoints == null) {
            controlPoints = new ControlPoints();
            controlPoints.load(getTimingPoints().timingPoints);
        }
        return controlPoints;
    }
}
