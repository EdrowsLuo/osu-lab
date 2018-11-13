package com.edplan.nso.ruleset.base.beatmap.parser;

import com.edplan.nso.parser.IniParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDecoder {

    IniParser parser;

    private List<String> warnings = new ArrayList<>();

    private List<String> errors = new ArrayList<>();

    public BaseDecoder() {

    }

    public void clear() {
        parser = null;
        warnings.clear();
        errors.clear();
    }

    protected void warning(String w) {
        warnings.add(w);
        System.out.println("warning - " + w);
    }

    protected void error(String e) {
        errors.add(e);
        System.out.println("error - " + e);
    }

    protected void prepareForParse(IniParser parser) {

    }

    protected abstract void onParse(IniParser parser, JSONObject config);

    public void decode(InputStream inputStream, JSONObject decodeConfig) throws IOException {
        clear();
        parser = new IniParser();
        parser.parse(inputStream);
        prepareForParse(parser);
        onParse(parser, decodeConfig);
    }

    public class OpenInfo {

        public void warning(String w) {
            BaseDecoder.this.warning(w);
        }

        public void error(String e) {
            BaseDecoder.this.error(e);
        }
    }
}