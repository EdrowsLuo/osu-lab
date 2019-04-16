package com.edplan.framework.utils.reflect;

import com.edplan.framework.utils.interfaces.Consumer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReflectHelper {

    private static HashMap<Class, HashMap<String, Field>> fieldsCache = new HashMap<>();

    public static Field getField(Class klass, String name) throws NoSuchFieldException {
        if (!fieldsCache.containsKey(klass)) {
            fieldsCache.put(klass, new HashMap<>());
        }
        HashMap<String, Field> cache = fieldsCache.get(klass);
        if (!cache.containsKey(name)) {
            Field field = klass.getDeclaredField(name);
            field.setAccessible(true);
            cache.put(name, field);
        }
        return cache.get(name);
    }

    public static Class getRealClass(Class klass) {
        if (klass.getName().contains("$")) {
            return getRealClass(klass.getSuperclass());
        } else {
            return klass;
        }
    }

    public static void ensureClass(Class... ks) {
        for (Class k : ks) {
            try {
                Class.forName(k.getCanonicalName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void forAllField(Class klass, Consumer<Field> consumer) {
        if (klass == Object.class) {
            return;
        }
        for (Field field : klass.getDeclaredFields()) {
            consumer.consume(field);
        }
        forAllField(klass.getSuperclass(), consumer);
    }

    public static List<Field> getAllField(Class klass) {
        List<Field> list = new ArrayList<>();
        forAllField(klass, list::add);
        return list;
    }

    public static Object get(Field field, Object o) {
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void set(Field field, Object o, Object value) {
        try {
            field.set(o,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
