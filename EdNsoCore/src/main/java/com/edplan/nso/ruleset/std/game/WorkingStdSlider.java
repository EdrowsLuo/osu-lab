package com.edplan.nso.ruleset.std.game;

import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.v2.StdSlider;

public class WorkingStdSlider extends WorkingStdGameObject<StdSlider> {

    public WorkingStdSlider(StdSlider gameObject, StdBeatmap beatmap) {
        super(gameObject, beatmap);
    }

    @Override
    public void applyToGameField(StdGameField gameField) {

    }
}
