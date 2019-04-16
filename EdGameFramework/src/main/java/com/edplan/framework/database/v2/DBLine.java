package com.edplan.framework.database.v2;

public interface DBLine {

    enum ValueType {
        NULL, INTEGER, FLOAT, STRING, BLOB,
    }

    String getString(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    String getString(int idx);

    int getInt(int idx);

    long getLong(int idx);

    float getFloat(int idx);

    ValueType getType(int idx);

    ValueType getType(String key);

    default <T> T as(Class<T> klass) {
        return SimpleDB.reflect(this, klass);
    }

    default int asInt() {
        switch (getType(0)) {
            case FLOAT:
                return Math.round(getFloat(0));
            case INTEGER:
                return getInt(0);
            case STRING:
                return Integer.parseInt(getString(0));
                default:
                    return 0;
        }
    }
}
