package com.edplan.nso.ruleset.base.beatmap.parser;

import com.edplan.nso.NsoCore;
import com.edplan.nso.parser.IniParser;
import com.edplan.nso.ruleset.base.beatmap.Beatmap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeatmapDecoder extends BaseDecoder{

    public static final Pattern OSU_FILE_FORMAT_PATTERN = Pattern.compile(" *osu file format v(\\d+) *");

    private static final String PAGE_GENERAL = "General";

    protected NsoCore core;

    protected int formatVersion = -1;

    protected String rulesetName;

    protected IniParser.StdOptionPage ganeralPage;


    public BeatmapDecoder(NsoCore core) {
        this.core = core;
    }

    @Override
    public void clear() {
        super.clear();
        formatVersion = -1;
        ganeralPage = null;
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
    protected void onParse(IniParser parser) {

        parseFormatLine(parser);
        if (!parser.hasPage(PAGE_GENERAL)) {
            error(String.format("missing part [%s]", PAGE_GENERAL));
            return;
        }

        ganeralPage = parser.asOptionPage(PAGE_GENERAL);
        if (!ganeralPage.hasKey("Mode")) {
            error("property \"Mode\" is required!");
            return;
        }

        /* 获取Ruleset */
        rulesetName = core.getRulesetNameManager().parseShortName(ganeralPage.getString("Mode"));

        Result result = new Result();

    }

    public static class Result {

        private boolean success = false;

        private Beatmap beatmap;

        public boolean isSuccess() {
            return success;
        }
    }
}
