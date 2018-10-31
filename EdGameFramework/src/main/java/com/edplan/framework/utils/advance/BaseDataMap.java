package com.edplan.framework.utils.advance;

import com.edplan.framework.utils.DataMap;

public abstract class BaseDataMap implements DataMap {

    @Override
    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    @Override
    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    @Override
    public boolean getBoolean(String key) {
        String data = getString(key);
        if ("true".equals(data)) {
            return true;
        }
        if ("false".equals(data)) {
            return false;
        }
        return Integer.parseInt(data) > 0;
    }

    @Override
    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    @Override
    public float getFloat(String key) {
        return Float.parseFloat(getString(key));
    }


}
