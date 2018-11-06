package com.edplan.framework.utils.dataobject;

import com.edplan.framework.utils.Getter;
import com.edplan.framework.utils.dataobject.def.DefaultBoolean;
import com.edplan.framework.utils.dataobject.def.DefaultDouble;
import com.edplan.framework.utils.dataobject.def.DefaultFloat;
import com.edplan.framework.utils.dataobject.def.DefaultInt;
import com.edplan.framework.utils.dataobject.def.DefaultLong;
import com.edplan.framework.utils.dataobject.def.DefaultString;
import com.edplan.framework.utils.reflect.ReflectHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StructAnnotationLoader {

    private static HashMap<Class, List<Struct.Loader>> structLoaders = new HashMap<>();

    private static <T,A extends Annotation> void createLoader(List<Struct.Loader> loaders,
                                                              Field field,
                                                              String info,
                                                              Class<T> klass,
                                                              Class<A> aklass,
                                                              Getter<T> defvalue) {
        if (field.isAnnotationPresent(aklass)) {
            loaders.add((o, s) -> s.add(
                    info, klass,
                    () -> (T) ReflectHelper.get(field, o),
                    i -> ReflectHelper.set(field, o, (T) i),
                    defvalue.get()));
        } else {
            loaders.add((o, s) -> s.add(
                    info, klass,
                    () -> (T) ReflectHelper.get(field, o),
                    i -> ReflectHelper.set(field, o, (T) i)));
        }
    }

    public static <T extends DataObject> void load(T obj,Struct struct) {
        Class k = obj.getClass();
        if (!structLoaders.containsKey(k)) {
            List<Struct.Loader> loaders = new ArrayList<>();
            ReflectHelper.forAllField(obj.getClass(), field -> {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ItemInfo.class)) {
                    if ((field.getModifiers() & Modifier.STATIC) == 0) {
                        ItemInfo infoA = field.getAnnotation(ItemInfo.class);
                        String info;
                        if (infoA.value().isEmpty()) {
                            info = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                        } else {
                            info = infoA.value();
                        }
                        Class klass = Item.safeTransform(field.getType());
                        if (!Item.ALLOWED_CLASS.contains(klass)) {
                            throw new RuntimeException("error annotation: illegal type!");
                        }
                        if (klass == Integer.class) {
                            createLoader(loaders, field, info, klass, DefaultInt.class,
                                    () -> field.getAnnotation(DefaultInt.class).value());
                        } else if (klass == Long.class) {
                            createLoader(loaders, field, info, klass, DefaultLong.class,
                                    () -> field.getAnnotation(DefaultLong.class).value());
                        } else if (klass == String.class) {
                            createLoader(loaders, field, info, klass, DefaultString.class,
                                    () -> field.getAnnotation(DefaultString.class).value());
                        } else if (klass == Boolean.class) {
                            createLoader(loaders, field, info, klass, DefaultBoolean.class,
                                    () -> field.getAnnotation(DefaultBoolean.class).value());
                        } else if (klass == Double.class) {
                            createLoader(loaders, field, info, klass, DefaultDouble.class,
                                    () -> field.getAnnotation(DefaultDouble.class).value());
                        } else if (klass == Float.class) {
                            createLoader(loaders, field, info, klass, DefaultFloat.class,
                                    () -> field.getAnnotation(DefaultFloat.class).value());
                        } else {
                            throw new IllegalArgumentException("unsupported class!");
                        }
                    }
                }
            });
            structLoaders.put(k, loaders);
        }
        for (Struct.Loader loader : structLoaders.get(k)) {
            loader.apply(obj, struct);
        }
    }
}
