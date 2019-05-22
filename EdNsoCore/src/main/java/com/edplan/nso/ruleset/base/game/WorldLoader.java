package com.edplan.nso.ruleset.base.game;

import com.edplan.nso.NsoCore;
import com.edplan.nso.NsoCoreBased;
import com.edplan.nso.ruleset.base.beatmap.BeatmapDescription;

import java.util.Set;

public abstract class WorldLoader extends NsoCoreBased{

    public WorldLoader(NsoCore core) {
        super(core);
    }

    public abstract Set<String> acceptedBeatmapType();

    public abstract World loadWorld(BeatmapDescription description);

}
