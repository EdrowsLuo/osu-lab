package com.edlplan.framework.config;

import com.edlplan.framework.config.json.JsonStringTable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class StringTable {

    public String get(String id) {
        return getRawStringById(id);
    }

    public String format(String format, Object... values) {
        return String.format(getRawStringById(format), values);
    }

    /**
     * 获取原始字符串
     * @param id 字符串id，建议通过 tableName:valueName 的格式访问
     * @return 对应的字符串，表里没有时返回null
     */
    protected abstract String getRawStringById(String id);

    public abstract boolean hasString(String id);

    public static StringTable fromJSON(JSONObject object) {
        return new JsonStringTable(object);
    }

    public static StringTable fromJSON(String json) throws JSONException {
        return new JsonStringTable(new JSONObject(json));
    }

}
