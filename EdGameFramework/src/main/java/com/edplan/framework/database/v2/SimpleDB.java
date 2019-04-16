package com.edplan.framework.database.v2;

import com.edplan.framework.database.v2.sqlite.SimpleSQLite;
import com.edplan.framework.utils.interfaces.Function;
import com.edplan.framework.utils.reflect.ReflectHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleDB {

    private static HashMap<Class,Function<DBLine,?>> reflectMap = new HashMap<>();

    static {
        registerReflect(Integer.class, DBLine::asInt);
    }

    public static ISimpleDB instance() {
        return new SimpleSQLite();
    }

    public static <T> void registerReflect(Class<T> klass, Function<DBLine, T> function) {
        reflectMap.put(klass, function);
    }

    @SuppressWarnings("unchecked")
    public static <T> T reflect(DBLine line, Class<T> klass) {
        if (!reflectMap.containsKey(klass)) {
            createReflect(klass);
        }
        return (T) reflectMap.get(klass).reflect(line);
    }

    @SuppressWarnings("unchecked")
    private static <T> void createReflect(Class<T> klass) {
        if (reflectMap.containsKey(klass)) {
            return;
        }

        if (klass.isAssignableFrom(DatabaseReflectable.class)) {
            try {
                registerReflect(klass, (Function<DBLine, T>) ((DatabaseReflectable)klass.newInstance()).getReflect());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        List<Field> list = new ArrayList<>();
        for (Field field : ReflectHelper.getAllField(klass)) {
            if (!field.isAnnotationPresent(DBIgnore.class)) {
                list.add(field);
                field.setAccessible(true);
            }
        }
        Field[] array = list.toArray(new Field[list.size()]);
        registerReflect(klass, line -> {
            try {
                T t = klass.newInstance();
                for (Field field : array) {
                    switch (line.getType(field.getName())) {
                        case BLOB:
                        case NULL:
                            break;
                        case INTEGER:
                            field.setInt(t, line.getInt(field.getName()));
                            break;
                        case FLOAT:
                            field.setFloat(t, line.getFloat(field.getName()));
                            break;
                        case STRING:
                            field.set(t, line.getString(field.getName()));
                            break;
                    }
                }
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public @interface DBIgnore { }

    public interface DatabaseReflectable {
        Function<DBLine, ?> getReflect();
    }

}
