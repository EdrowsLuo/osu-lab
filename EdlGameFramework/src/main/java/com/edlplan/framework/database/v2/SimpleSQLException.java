package com.edlplan.framework.database.v2;

public class SimpleSQLException extends Exception {
    public SimpleSQLException(String msg) {
        super(msg);
    }

    public SimpleSQLException(String msg, Throwable c) {
        super(msg, c);
    }
}
