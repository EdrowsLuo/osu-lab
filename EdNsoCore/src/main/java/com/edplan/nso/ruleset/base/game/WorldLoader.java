package com.edplan.nso.ruleset.base.game;

import com.edplan.nso.ruleset.base.beatmap.BeatmapDescription;

import org.json.JSONObject;

import java.util.Set;

public abstract class WorldLoader {

    public abstract Set<String> acceptedBeatmapType();

    public abstract World loadWorld(BeatmapDescription description, JSONObject config);

}
