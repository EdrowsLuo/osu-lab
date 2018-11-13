package com.edplan.nso.parser;

public class ParseException extends Exception{
    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
