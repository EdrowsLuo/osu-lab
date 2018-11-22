package com.edplan.framework.utils.dataobject;

import java.util.HashMap;

public class TypeData {

    private static HashMap<Class<?>, Converter<?>> converterMap = new HashMap<>();

    static {
        registerConverter(Integer.class, Integer::parseInt);
        registerConverter(Long.class, Long::parseLong);
        registerConverter(Boolean.class, TypeData::parseBoolean);
        registerConverter(Double.class, Double::parseDouble);
        registerConverter(Float.class, Float::parseFloat);
        registerConverter(String.class, s -> s);
    }

    public static <T> void registerConverter(Class<T> klass, Converter<T> converter) {
        converterMap.put(klass, converter);
    }

    public static <T> T convert(Class<?> klass,String res) {
        return (T) converterMap.get(klass).converter(res);
    }

    public static boolean parseBoolean(String data) {
        if ("true".equals(data)) {
            return true;
        }
        if ("false".equals(data)) {
            return false;
        }
        return Integer.parseInt(data) > 0;
    }

    @FunctionalInterface
    public interface Converter<T>{
        T converter(String res);
    }
}
