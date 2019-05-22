package com.edlplan.nso.ruleset.std.objects.v2.direct;

import com.edlplan.nso.difficulty.DifficultyUtil;
import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;

public abstract class DirectStdGameObject {

    protected StdBeatmap beatmap;

    protected StdGameProperty property;

    public DirectStdGameObject(StdBeatmap beatmap, StdGameProperty property) {
        this.beatmap = beatmap;
        this.property = property;
    }

    public StdGameProperty getProperty() {
        return property;
    }

    public void setProperty(StdGameProperty property) {
        this.property = property;
    }

    public StdBeatmap getBeatmap() {
        return beatmap;
    }

    public void setBeatmap(StdBeatmap beatmap) {
        this.beatmap = beatmap;
    }

    protected double getSizeAfterCS() {
        return getProperty().getBaseSize() * DifficultyUtil.stdCircleSizeScale(getBeatmap().getDifficulty().getCircleSize());
    }

}
