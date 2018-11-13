package com.edplan.framework.utils;

import com.edplan.framework.utils.advance.BaseDataMap;

import java.util.HashMap;
import java.util.Set;

public class HashDataMap extends BaseDataMap {

    public HashMap<String, String> data = new HashMap<>();

    @Override
    public Set<String> keys() {
        return data.keySet();
    }

    @Override
    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    @Override
    public String getString(String key) {
        return data.get(key);
    }
}
