package com.edplan.framework.config;

import com.edplan.framework.utils.structobj.StructObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigList implements StructObject {
    private HashMap<String, ConfigEntry> data = new HashMap<String, ConfigEntry>();

    private ArrayList<ConfigEntry> dataList = new ArrayList<ConfigEntry>();

    private String listName;

    public void setDataList(ArrayList<ConfigEntry> dataList) {
        this.dataList = dataList;
    }

    public Iterator<ConfigEntry> iterateProperty() {
        return dataList.iterator();
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }

    public ConfigEntry putBoolean(String name, boolean b) {
        if (data.containsKey(name)) {
            ConfigEntry e = data.get(name);
            e.asBoolean().set(b);
            return e;
        } else {
            ConfigEntry e = new ConfigEntry();
            e.setName(name);
            e.asBoolean().set(b);
            data.put(name, e);
            dataList.add(e);
            return e;
        }
    }

    @Override
    public JSONObject asJson() {

        JSONObject robj = new JSONObject();
        try {
            robj.put("name", listName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        for (ConfigEntry e : dataList) {
            try {
                obj.put(e.getName(), e.asJson());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        try {
            robj.put("data", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return robj;
    }

    @Override
    public void injectJson(JSONObject data) {

        listName = data.optString("name");
        data = data.optJSONObject("data");
        final JSONArray ary = data.names();
        final int count = ary.length();
        for (int i = 0; i < count; i++) {
            final ConfigEntry p = new ConfigEntry();
            try {
                String name = ary.getString(i);
                p.injectJson(data.getJSONObject(name));
                this.data.put(ary.getString(i), p);
                this.dataList.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
