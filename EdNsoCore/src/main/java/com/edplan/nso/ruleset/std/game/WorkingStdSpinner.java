package com.edplan.nso.ruleset.std.game;

import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.v2.StdSpinner;

public class WorkingStdSpinner extends WorkingStdGameObject<StdSpinner> {

    public WorkingStdSpinner(StdSpinner gameObject, StdBeatmap beatmap) {
        super(gameObject, beatmap);
    }

    @Override
    public void applyToGameField(StdGameField gameField) {

    }
}
