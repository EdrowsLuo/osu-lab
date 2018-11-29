package com.edplan.framework.utils.dataobject;

import com.edplan.framework.utils.interfaces.Getter;
import com.edplan.framework.utils.interfaces.Setter;

import java.util.ArrayList;
import java.util.List;

public final class Struct {

    ArrayList<Item<?>> items = new ArrayList<>();

    public <T> Struct add(String name, Class<T> klass, Getter<T> getter, Setter<T> setter) {
        items.add(new Item<>(name, klass, getter, setter));
        return this;
    }

    public <T> Struct add(String name,Class<T> klass, Getter<T> getter, Setter<T> setter, T defvalue) {
        items.add(new Item<>(name, klass, getter, setter, defvalue));
        return this;
    }

    public List<Item<?>> getItems() {
        return items;
    }

    @FunctionalInterface
    public interface Loader{
        void apply(DataMapObject object, Struct struct);
    }
}
