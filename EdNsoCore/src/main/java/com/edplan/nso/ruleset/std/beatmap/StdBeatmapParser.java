package com.edplan.nso.ruleset.std.beatmap;

import com.edplan.nso.NsoCore;
import com.edplan.nso.parser.IniParser;
import com.edplan.nso.ruleset.base.beatmap.parser.BaseDecoder;
import com.edplan.nso.ruleset.base.beatmap.parser.BeatmapParser;

import org.json.JSONObject;

public class StdBeatmapParser implements BeatmapParser<StdBeatmap> {

    @Override
    public StdBeatmap parse(NsoCore core, int formatVersion, String ruleset,
                            IniParser.StdOptionPage generalCache, IniParser parserData,
                            BaseDecoder.OpenInfo info, JSONObject config) {
        return null;
    }
}
