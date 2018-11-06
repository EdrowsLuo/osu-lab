package com.edplan.nso.ruleset.base.beatmap.parser;

import com.edplan.nso.NsoCore;
import com.edplan.nso.parser.IniParser;
import com.edplan.nso.ruleset.base.Ruleset;
import com.edplan.nso.ruleset.base.beatmap.Beatmap;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeatmapDecoder extends BaseDecoder{

    public static final Pattern OSU_FILE_FORMAT_PATTERN = Pattern.compile(" *osu file format v(\\d+) *");

    private static final String PAGE_GENERAL = "General";

    protected NsoCore core;

    protected int formatVersion = -1;

    protected String rulesetId;

    protected IniParser.StdOptionPage ganeralPage;

    protected Result result;


    public BeatmapDecoder(NsoCore core) {
        this.core = core;
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
            Matcher matcher = OSU_FILE_FORMAT_PATTERN.matcher(line);
            if (matcher.groupCount() > 1) {
                formatVersion = Integer.parseInt(matcher.group(1));
                return;
            }
        }
        warning("format line not found!");
    }

    @Override
    protected void onParse(IniParser parser, JSONObject config) {

        Result result = new Result();

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
                .parse(core, formatVersion, rulesetId, ganeralPage, parser, new OpenInfo(), config);

        if (beatmap == null) {
            result.success = false;
        }
    }

    public static class Result {

        private boolean success = false;

        private Beatmap beatmap;

        private String rulesetId;

        public boolean isSuccess() {
            return success;
        }
    }

    public static class Config {
        public static final String PARSE_STORYBOARD = "PARSE_STORYBOARD";
    }
}
