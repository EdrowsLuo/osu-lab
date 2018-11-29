package com.edplan.framework.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonBasedObject {
    JSONObject asJson();

    void injectJson(JSONObject data) throws JSONException;
}
