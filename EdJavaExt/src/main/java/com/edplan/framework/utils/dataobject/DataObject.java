package com.edplan.framework.utils.dataobject;

public abstract class DataObject {

    private Struct struct;

    protected abstract void onLoadStruct(Struct struct);

    public Struct getStruct() {
        if (struct == null) {
            struct = new Struct();
            onLoadStruct(struct);
        }
        return struct;
    }

    protected void loadStructAnnotation(Struct struct) {
        StructAnnotationLoader.load(this, struct);
    }

    public String asStructString(StructOutputType type) {
        switch (type) {
            case INI:
                return toIniPage();
            case JSON:default:
                return null;
        }
    }

    private String toIniPage() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Item item : struct.getItems()) {
            stringBuilder.append(item.name).append(": ").append(item.getter.get()).append('\n');
        }
        return stringBuilder.toString();
    }

    public enum StructOutputType{
        INI,JSON
    }
}
