package com.edplan.nso.ruleset.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesetNameManager {

    private Map<String, String> shortNameToIdName = new HashMap<>();

    private Map<String, String> idNameToShortName = new HashMap<>();

    private List<String> names = new ArrayList<>();

    public RulesetNameManager() {
        putNameRef("0", "ppy.osu.Std");
    }

    public void putNameRef(String shortName, String idName) {
        shortNameToIdName.put(shortName, idName);
        idNameToShortName.put(idName, shortName);
        names.add(idName);
    }

    public String parseShortName(String name) {
        if (shortNameToIdName.containsKey(name)) {
            return shortNameToIdName.get(name);
        } else {
            return name;
        }
    }
}
