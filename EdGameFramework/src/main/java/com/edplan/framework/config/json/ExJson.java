package com.edplan.framework.config.json;

import com.edplan.framework.resource.AResource;

import org.json.JSONException;
import org.json.JSONObject;

public class ExJson {

    public static final String KEY_NAMESPACE = "namespace";

    protected AResource resource;

    protected JSONObject json;

    public ExJson(AResource resource) {
        this.resource = resource;
        json = new JSONObject();
    }

    public ExJson(String data) throws JSONException {
        json = new JSONObject(data);
    }

    public boolean hasCustomNamespace() {
        return json.has(KEY_NAMESPACE);
    }

    public String namespcae() throws JSONException {
        return hasCustomNamespace() ? json.getString(KEY_NAMESPACE) : "";
    }




}
