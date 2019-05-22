package com.edlplan.nso.ruleset.base.game;

import com.edlplan.nso.NsoCore;
import com.edlplan.nso.NsoCoreBased;
import com.edlplan.nso.ruleset.base.beatmap.BeatmapDescription;

import java.util.Set;

public abstract class WorldLoader extends NsoCoreBased {

    public WorldLoader(NsoCore core) {
        super(core);
    }

    public abstract Set<String> acceptedBeatmapType();

    public abstract World loadWorld(BeatmapDescription description);

}
