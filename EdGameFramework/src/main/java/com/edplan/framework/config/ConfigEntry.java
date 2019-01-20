package com.edplan.framework.config;

import com.edplan.framework.config.property.ConfigBoolean;
import com.edplan.framework.config.property.ConfigInteger;
import com.edplan.framework.config.property.ConfigProperty;
import com.edplan.framework.config.property.ConfigString;
import com.edplan.framework.utils.JsonBasedObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class ConfigEntry implements JsonBasedObject {
    private ConfigProperty property;

    private String name;

    private List<String> tags;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return (property == null) ? ConfigProperty.TYPE_NULL : property.propertyType();
    }

    public ConfigBoolean asBoolean() {
        if (property == null) property = new ConfigBoolean();
        return (ConfigBoolean) property;
    }

    public ConfigString asString() {
        if (property == null) property = new ConfigString();
        return (ConfigString) property;
    }

    public ConfigInteger asInt() {
        if (property == null) property = new ConfigInteger();
        return (ConfigInteger) property;
    }

    public boolean hasTag(String tag) {
        if (tags == null) return false;
        return tags.contains(tag);
    }

    public boolean hasAllTag(String... ts) {
        if (tags == null) return false;
        return tags.containsAll(Arrays.asList(ts));
    }

    public void addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(String t) {
        if (tags == null) return;
        tags.remove(t);
        if (tags.size() == 0) tags = null;
    }

    @Override
    public JSONObject asJson() {

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("type", property.propertyType());
            if (tags != null) {
                for (String s : tags) {
                    json.accumulate("tags", s);
                }
            }
            json.put("data", property.asJson());
        } catch (JSONException e) {
        }
        return json;
    }

    @Override
    public void injectJson(JSONObject data) {

        name = data.optString("name");
        final String type = data.optString("type");
        property = ConfigProperty.loadProperty(type, data.optJSONObject("data"));
        final JSONArray tagary = data.optJSONArray("tags");
        if (tagary != null) {
            int count = tagary.length();
            for (int i = 0; i < count; i++) {
                addTag(tagary.optString(i));
            }
        }
    }
}
