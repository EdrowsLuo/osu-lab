package com.edlplan.nso.ruleset.std.beatmap;

import com.edlplan.nso.NsoCore;
import com.edlplan.nso.parser.IniParser;
import com.edlplan.nso.ruleset.base.beatmap.parser.BaseBeatmapParser;
import com.edlplan.nso.ruleset.base.beatmap.parser.BaseDecoder;
import com.edlplan.nso.ruleset.base.beatmap.parser.BeatmapParser;
import com.edlplan.nso.ruleset.base.beatmap.parser.StdFormatObjectParser;
import com.edlplan.nso.ruleset.std.objects.v2.StdObjectCreator;
import com.edlplan.framework.test.performance.Tracker;
import com.edlplan.framework.utils.Behavior;

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
                            BaseDecoder.OpenInfo info) {
        StdBeatmap beatmap = new StdBeatmap();
        Behavior.wrap(() -> {
            Behavior.wrap(() ->
                    BaseBeatmapParser.parseDefault(
                            beatmap, core, formatVersion,
                            ruleset, generalCache, parserData,
                            info))
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
