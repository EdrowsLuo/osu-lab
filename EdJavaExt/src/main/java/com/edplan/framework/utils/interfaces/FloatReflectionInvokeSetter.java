package com.edplan.framework.utils.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FloatReflectionInvokeSetter<T> implements FloatInvokeSetter<T> {
    private Method setter;

    public FloatReflectionInvokeSetter(T target, String propertyName) {
        Class klass = target.getClass();
        try {
            setter = klass.getMethod(makeMethodName(propertyName), float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void invoke(T target, float v) {

        try {
            setter.invoke(target, v);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String makeMethodName(String name) {
        if (name.startsWith("set")) {
            return name;
        } else {
            return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
        }
    }

    public static String makeGetMethodName(String name) {
        if (name.startsWith("get")) {
            return name;
        } else {
            return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
        }
    }
}
