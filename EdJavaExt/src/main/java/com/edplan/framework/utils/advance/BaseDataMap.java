package com.edplan.framework.utils.advance;

import com.edplan.framework.utils.DataMap;
import com.edplan.framework.utils.dataobject.DataMapObject;
import com.edplan.framework.utils.dataobject.Item;
import com.edplan.framework.utils.dataobject.Struct;

public abstract class BaseDataMap implements DataMap {

    public String getString(String key,String def) {
        return hasKey(key) ? getString(key) : def;
    }

    @Override
    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public int getInt(String key, int def) {
        return hasKey(key) ? getInt(key) : def;
    }

    @Override
    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    public long getLong(String key, long def) {
        return hasKey(key) ? getLong(key) : def;
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

    public boolean getBoolean(String key, boolean def) {
        return hasKey(key) ? getBoolean(key) : def;
    }

    @Override
    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public double getDouble(String key, double def) {
        return hasKey(key) ? getDouble(key) : def;
    }

    @Override
    public float getFloat(String key) {
        return Float.parseFloat(getString(key));
    }

    public float getFloat(String key, float def) {
        return hasKey(key) ? getFloat(key) : def;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> klass, T defvalue) {
        if (defvalue == null) {
            return getValue(key, klass);
        }
        if (klass == Integer.class) {
            return (T) (Integer) getInt(key, (Integer) defvalue);
        } else if (klass == Long.class) {
            return (T) (Long) getLong(key, (Long) defvalue);
        } else if (klass == String.class) {
            return (T) getString(key, (String) defvalue);
        } else if (klass == Boolean.class) {
            return (T) (Boolean) getBoolean(key, (Boolean) defvalue);
        } else if (klass == Double.class) {
            return (T) (Double) getDouble(key, (Double) defvalue);
        } else if (klass == Long.class) {
            return (T) (Float) getFloat(key, (Float) defvalue);
        } else {
            throw new IllegalArgumentException("unsupported class!");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> klass) {
        if (klass == Integer.class) {
            return (T) (Integer) getInt(key);
        } else if (klass == Long.class) {
            return (T) (Long) getLong(key);
        } else if (klass == String.class) {
            return (T) getString(key);
        } else if (klass == Boolean.class) {
            return (T) (Boolean) getBoolean(key);
        } else if (klass == Double.class) {
            return (T) (Double) getDouble(key);
        } else if (klass == Long.class) {
            return (T) (Float) getFloat(key);
        } else {
            throw new IllegalArgumentException("unsupported class!");
        }
    }

    public void load(DataMapObject object) {
        loadStruct(object.getStruct());
    }

    @SuppressWarnings("unchecked")
    public void loadStruct(Struct object) {
        for (Item<?> item : object.getItems()) {
            Class<?> klass = item.klass;
            if (item.defvalue != null) {
                if (klass == Integer.class) {
                    Item<Integer> tmp = (Item<Integer>) item;
                    tmp.setter.set(getInt(tmp.name, tmp.defvalue));
                } else if (klass == Long.class) {
                    Item<Long> tmp = (Item<Long>) item;
                    tmp.setter.set(getLong(tmp.name, tmp.defvalue));
                } else if (klass == String.class) {
                    Item<String> tmp = (Item<String>) item;
                    tmp.setter.set(getString(tmp.name, tmp.defvalue));
                } else if (klass == Boolean.class) {
                    Item<Boolean> tmp = (Item<Boolean>) item;
                    tmp.setter.set(getBoolean(tmp.name, tmp.defvalue));
                } else if (klass == Double.class) {
                    Item<Double> tmp = (Item<Double>) item;
                    tmp.setter.set(getDouble(tmp.name, tmp.defvalue));
                } else if (klass == Float.class) {
                    Item<Float> tmp = (Item<Float>) item;
                    tmp.setter.set(getFloat(tmp.name, tmp.defvalue));
                } else {
                    throw new IllegalArgumentException("unsupported class!");
                }
            } else {
                if (klass == Integer.class) {
                    Item<Integer> tmp = (Item<Integer>) item;
                    tmp.setter.set(getInt(tmp.name));
                } else if (klass == Long.class) {
                    Item<Long> tmp = (Item<Long>) item;
                    tmp.setter.set(getLong(tmp.name));
                } else if (klass == String.class) {
                    Item<String> tmp = (Item<String>) item;
                    tmp.setter.set(getString(tmp.name));
                } else if (klass == Boolean.class) {
                    Item<Boolean> tmp = (Item<Boolean>) item;
                    tmp.setter.set(getBoolean(tmp.name));
                } else if (klass == Double.class) {
                    Item<Double> tmp = (Item<Double>) item;
                    tmp.setter.set(getDouble(tmp.name));
                } else if (klass == Float.class) {
                    Item<Float> tmp = (Item<Float>) item;
                    tmp.setter.set(getFloat(tmp.name));
                } else {
                    throw new IllegalArgumentException("unsupported class!");
                }
            }

        }
    }


}
