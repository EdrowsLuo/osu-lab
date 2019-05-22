package com.edlplan.framework.config.json;

import com.edlplan.framework.config.StringTable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonStringTable extends StringTable{

    private HashMap<String, String> table = new HashMap<>();

    public JsonStringTable(JSONObject array) {
        Iterator<String> keys = array.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            table.put(key, array.optString(key));
        }
    }

    @Override
    protected String getRawStringById(String id) {
        return table.get(id);
    }

    @Override
    public boolean hasString(String id) {
        return table.containsKey(id);
    }

}
