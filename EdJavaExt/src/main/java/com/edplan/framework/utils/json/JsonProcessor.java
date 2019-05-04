package com.edplan.framework.utils.json;

public abstract class JsonProcessor {

    private JsonStream stream;

    public JsonProcessor(JsonStream stream) {
        this.stream = stream;
    }

    public abstract void processor();







}
