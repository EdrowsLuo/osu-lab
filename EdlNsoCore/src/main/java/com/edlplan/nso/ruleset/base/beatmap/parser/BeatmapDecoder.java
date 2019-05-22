package com.edlplan.nso.ruleset.base.beatmap.parser;

import com.edlplan.nso.NsoCore;
import com.edlplan.nso.parser.IniParser;
import com.edlplan.nso.ruleset.base.Ruleset;
import com.edlplan.nso.ruleset.base.beatmap.Beatmap;

import java.util.regex.Pattern;

public class BeatmapDecoder extends BaseDecoder{

    public static final Pattern OSU_FILE_FORMAT_PATTERN = Pattern.compile(" *osu file format v(\\d+) *");

    public static final String OSU_FILE_FORMAT = "osu file format v";

    private static final String PAGE_GENERAL = "General";

    protected NsoCore core;

    protected int formatVersion = -1;

    protected String rulesetId;

    protected IniParser.StdOptionPage ganeralPage;

    protected Result result;

    public BeatmapDecoder(NsoCore core) {
        this.core = core;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public void clear() {
        super.clear();
        formatVersion = -1;
        ganeralPage = null;
        result = null;
    }

    @Override
    protected void prepareForParse(IniParser parser) {
        super.prepareForParse(parser);
    }

    protected void parseFormatLine(IniParser parser) {
        for (String line : parser.getHeader()) {
            line = line.trim();
            if (line.startsWith(OSU_FILE_FORMAT)) {
                formatVersion = Integer.parseInt(line.substring(OSU_FILE_FORMAT.length()));
                return;
            }
        }
        warning("format line not found!");
    }

    @Override
    protected void onParse(IniParser parser) {

        result = new Result();

        parseFormatLine(parser);
        if (!parser.hasPage(PAGE_GENERAL)) {
            error(String.format("missing part [%s]", PAGE_GENERAL));
            result.success = false;
            return;
        }

        ganeralPage = parser.asOptionPage(PAGE_GENERAL);
        if (!ganeralPage.hasKey("Mode")) {
            error("property \"Mode\" is required!");
            result.success = false;
            return;
        }

        /* 获取Ruleset */
        rulesetId = core.getRulesetNameManager().parseIdName(ganeralPage.getString("Mode"));

        result.rulesetId = rulesetId;

        Ruleset ruleset = core.getRulesetById(rulesetId);
        if (ruleset == null) {
            error(String.format("ruleset \"%s\" not found"));
            result.success = false;
            return;
        }


        Beatmap beatmap = ruleset.getBeatmapParser()
                .parse(core, formatVersion, rulesetId, ganeralPage, parser, new OpenInfo());

        if (beatmap == null) {
            result.success = false;
        } else {
            result.success = true;
            result.beatmap = beatmap;
        }
    }

    public static class Result {

        private boolean success = false;

        private Beatmap beatmap;

        private String rulesetId;

        public boolean isSuccess() {
            return success;
        }

        public String getRulesetId() {
            return rulesetId;
        }

        public Beatmap getBeatmap() {
            return beatmap;
        }
    }

    public static class Config {
        public static final String PARSE_STORYBOARD = "PARSE_STORYBOARD";
    }
}
