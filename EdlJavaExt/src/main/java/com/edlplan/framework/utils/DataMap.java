package com.edlplan.framework.utils;

import java.util.Set;

/**
 * 获取各种格式数据的map
 */
public interface DataMap {

    Set<String> keys();

    boolean hasKey(String key);

    String getString(String key);

    int getInt(String key);

    long getLong(String key);

    boolean getBoolean(String key);

    double getDouble(String key);

    float getFloat(String key);

}
