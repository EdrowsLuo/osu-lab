package com.edlplan.framework.utils;

import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public interface JsonBasedObject {
    JSONObject asJson();

    void injectJson(JSONObject data) throws JSONException;
}
