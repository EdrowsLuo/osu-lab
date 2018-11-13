package com.edplan.nso.ruleset.std.beatmap;

import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.utils.Behavior;
import com.edplan.nso.NsoCore;
import com.edplan.nso.parser.IniParser;
import com.edplan.nso.ruleset.base.beatmap.parser.BaseBeatmapParser;
import com.edplan.nso.ruleset.base.beatmap.parser.BaseDecoder;
import com.edplan.nso.ruleset.base.beatmap.parser.BeatmapParser;
import com.edplan.nso.ruleset.base.beatmap.parser.StdFormatObjectParser;
import com.edplan.nso.ruleset.std.objects.v2.StdObjectCreator;

import org.json.JSONObject;

public class StdBeatmapParser implements BeatmapParser<StdBeatmap> {

    private static StdBeatmapParser parser = new StdBeatmapParser();

    private static StdFormatObjectParser objectParser = new StdFormatObjectParser();

    public static StdBeatmapParser get() {
        return parser;
    }

    public static void load() {
        objectParser.register(StdObjectCreator.getInstance());
    }

    @Override
    public StdBeatmap parse(NsoCore core, int formatVersion, String ruleset,
                            IniParser.StdOptionPage generalCache, IniParser parserData,
                            BaseDecoder.OpenInfo info, JSONObject config) {
        StdBeatmap beatmap = new StdBeatmap();
        Behavior.wrap(() -> {
            Behavior.wrap(() ->
                    BaseBeatmapParser.parseDefault(
                            beatmap, core, formatVersion,
                            ruleset, generalCache, parserData,
                            info, config))
                    .countTime(Tracker.printByTag("ParseDefault"))
                    .run();

            Behavior.wrap(() ->
                    BaseBeatmapParser.parseStdFormatHitObjects(
                            beatmap, parserData, info, objectParser))
                    .countTime(Tracker.printByTag("ParseObjects"))
                    .run();
            }).countTime(Tracker.printByTag("ParseTotal")).run();
        return beatmap;
    }
}
