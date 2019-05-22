package com.edlplan.framework.utils.dataobject;

import com.edlplan.framework.utils.interfaces.Getter;
import com.edlplan.framework.utils.interfaces.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Item<T> {
    public static Set<Class> ALLOWED_CLASS = new HashSet<>();
    public static HashMap<Class, Class> SAFE_TRANSFORM = new HashMap<>();
    static {
        ALLOWED_CLASS.add(Integer.class);
        ALLOWED_CLASS.add(Long.class);
        ALLOWED_CLASS.add(String.class);
        ALLOWED_CLASS.add(Boolean.class);
        ALLOWED_CLASS.add(Double.class);
        ALLOWED_CLASS.add(Float.class);
        ALLOWED_CLASS.add(DataMapObject.class);

        SAFE_TRANSFORM.put(int.class, Integer.class);
        SAFE_TRANSFORM.put(long.class, Long.class);
        SAFE_TRANSFORM.put(boolean.class, Boolean.class);
        SAFE_TRANSFORM.put(double.class, Double.class);
        SAFE_TRANSFORM.put(float.class, Float.class);
    }

    public final String name;
    public final Class<T> klass;
    public final Getter<T> getter;
    public final Setter<T> setter;
    public final T defvalue;

    public Item(String name,Class<T> klass, Getter<T> getter, Setter<T> setter) {
        this.name = name;
        this.klass = klass;
        this.getter = getter;
        this.setter = setter;
        this.defvalue = null;
        if (!ALLOWED_CLASS.contains(klass)) {
            throw new RuntimeException("illegal class!");
        }
    }

    public Item(String name,Class<T> klass, Getter<T> getter, Setter<T> setter,T defvalue) {
        this.name = name;
        this.klass = klass;
        this.getter = getter;
        this.setter = setter;
        this.defvalue = defvalue;
        if (!ALLOWED_CLASS.contains(klass)) {
            throw new RuntimeException("illegal class!");
        }
    }

    public static Class safeTransform(Class klass) {
        return SAFE_TRANSFORM.containsKey(klass) ? SAFE_TRANSFORM.get(klass) : klass;
    }
}
