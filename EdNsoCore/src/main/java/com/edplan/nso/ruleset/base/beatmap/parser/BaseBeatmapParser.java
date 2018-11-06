package com.edplan.nso.ruleset.base.beatmap.parser;

import com.edplan.nso.NsoCore;
import com.edplan.nso.filepart.PartColours;
import com.edplan.nso.filepart.PartDifficulty;
import com.edplan.nso.filepart.PartEditor;
import com.edplan.nso.filepart.PartMetadata;
import com.edplan.nso.filepart.PartTimingPoints;
import com.edplan.nso.parser.IniParser;
import com.edplan.nso.ruleset.base.Ruleset;
import com.edplan.nso.ruleset.base.beatmap.Beatmap;

import org.json.JSONObject;

public class BaseBeatmapParser {

    /**
     * 按照默认方式解析除了HitObjects以外的所有部分
     * @param beatmap
     * @param core
     * @param formatVersion
     * @param rulesetId
     * @param generalCache
     * @param parserData
     * @param info
     * @param config
     * @return
     */
    public static boolean parseDefault(Beatmap beatmap, NsoCore core, int formatVersion,
                                       String rulesetId, IniParser.StdOptionPage generalCache,
                                       IniParser parserData, BaseDecoder.OpenInfo info,
                                       JSONObject config) {
        Ruleset ruleset = core.getRulesetById(rulesetId);
        PartFactory factory = ruleset.getPartFactory();

        beatmap.setRulesetId(rulesetId);
        beatmap.setGeneral(factory.createPartGeneral(generalCache, info));
        beatmap.setEditor(factory.createPartEditor(parserData.asOptionPage(PartEditor.TAG), info));
        beatmap.setMetadata(factory.createPartMetadata(parserData.asOptionPage(PartMetadata.TAG), info));
        beatmap.setDifficulty(factory.createPartDifficulty(parserData.asOptionPage(PartDifficulty.TAG), info));
        beatmap.setColours(factory.createPartColors(parserData.asOptionPage(PartColours.TAG), info));
        beatmap.setTimingPoints(factory.createPartTimingPoints(parserData.asCSVPage(PartTimingPoints.TAG), info));

        return true;
    }
}
