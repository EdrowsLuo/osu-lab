package com.edlplan.framework.config.property;

import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class ConfigInteger extends ConfigProperty {
    public static final String TYPE = "int";

    static {
        ConfigProperty.registerLoader(TYPE, obj -> {
            ConfigBoolean b = new ConfigBoolean();
            b.injectJson(obj);
            return b;
        });
    }

    private int data;

    public int get() {
        return data;
    }

    public void set(int b) {
        this.data = b;
    }

    @Override
    public String propertyType() {

        return TYPE;
    }

    @Override
    public JSONObject asJson() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("data", data);
        } catch (JSONException e) {
        }
        return obj;
    }

    @Override
    public void injectJson(JSONObject data) {

        set(data.optInt("data"));
    }
}
