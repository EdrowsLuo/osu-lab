package com.edplan.nso.ruleset.mania;

import com.edplan.nso.ruleset.std.LegacyStdBeatmap;

public class ManiaBeatmap extends LegacyStdBeatmap {
    public ManiaBeatmap(LegacyStdBeatmap res) {
        setVersion(res.getVersion());
        setGeneral(res.getGeneral());
        setEditor(res.getEditor());
        setMetadata(res.getMetadata());
        setDifficulty(res.getDifficulty());
        setEvent(res.getEvent());
        setTimingPoints(res.getTimingPoints());
        setColours(res.getColours());
        setHitObjects(res.getHitObjects());
    }
}
