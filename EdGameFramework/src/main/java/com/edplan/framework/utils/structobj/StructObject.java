package com.edplan.framework.utils.structobj;

import org.json.JSONException;
import org.json.JSONObject;

public interface StructObject {
    public JSONObject asJson();

    public void injectJson(JSONObject data) throws JSONException;
}
