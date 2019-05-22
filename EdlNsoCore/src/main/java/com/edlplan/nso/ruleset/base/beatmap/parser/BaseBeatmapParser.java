package com.edlplan.nso.ruleset.base.beatmap.parser;

import com.edlplan.nso.filepart.PartColours;
import com.edlplan.nso.filepart.PartDifficulty;
import com.edlplan.nso.filepart.PartEditor;
import com.edlplan.nso.filepart.PartHitObjects;
import com.edlplan.nso.filepart.PartMetadata;
import com.edlplan.nso.filepart.PartTimingPoints;
import com.edlplan.nso.NsoCore;
import com.edlplan.nso.parser.IniParser;
import com.edlplan.nso.ruleset.base.Ruleset;
import com.edlplan.nso.ruleset.base.beatmap.Beatmap;
import com.edlplan.nso.ruleset.base.game.StdFormatGameObject;
import com.edlplan.framework.utils.advance.StringSplitter;

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
     * @return
     */
    public static boolean parseDefault(Beatmap beatmap, NsoCore core, int formatVersion,
                                       String rulesetId, IniParser.StdOptionPage generalCache,
                                       IniParser parserData, BaseDecoder.OpenInfo info) {
        Ruleset ruleset = core.getRulesetById(rulesetId);
        PartFactory factory = ruleset.getPartFactory();

        //Tracker.createTmpNode("parseExpression-parts").wrap(() -> {
            beatmap.setRulesetId(rulesetId);
            beatmap.setGeneral(factory.createPartGeneral(generalCache, info));
            beatmap.setEditor(factory.createPartEditor(parserData.asOptionPage(PartEditor.TAG), info));
            beatmap.setMetadata(factory.createPartMetadata(parserData.asOptionPage(PartMetadata.TAG), info));
            beatmap.setDifficulty(factory.createPartDifficulty(parserData.asOptionPage(PartDifficulty.TAG), info));
            beatmap.setColours(factory.createPartColors(parserData.asOptionPage(PartColours.TAG), info));
            beatmap.setTimingPoints(factory.createPartTimingPoints(parserData.asCSVPage(PartTimingPoints.TAG), info));
        //}).then(System.out::println);

        return true;
    }

    public static boolean parseStdFormatHitObjects(Beatmap beatmap, IniParser parserData, BaseDecoder.OpenInfo info, StdFormatObjectParser parser) {
        for (String line : parserData.getPageByTag(PartHitObjects.TAG).lines) {
            if (line.isEmpty()) {
                continue;
            }
            StdFormatGameObject object = parser.parse(new StringSplitter(line, ","), info);
            if (object != null) {
                beatmap.addHitObject(object);
            } else {
                return false;
            }
        }
        return true;
    }
}
