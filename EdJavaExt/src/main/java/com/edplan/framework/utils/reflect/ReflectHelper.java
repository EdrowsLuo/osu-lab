package com.edplan.framework.utils.reflect;

import com.edplan.framework.utils.Consumer;

import java.lang.reflect.Field;

public class ReflectHelper {

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
