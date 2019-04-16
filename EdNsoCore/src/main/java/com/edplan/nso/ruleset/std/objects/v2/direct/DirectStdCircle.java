package com.edplan.nso.ruleset.std.objects.v2.direct;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdCircle;

public class DirectStdCircle {

    private Vec2 position = new Vec2();

    private float scale = 1;

    private double hitTime; //the time when the circle should be hit

    public DirectStdCircle(StdCircle circle, StdBeatmap beatmap) {

    }
}
