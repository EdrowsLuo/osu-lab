package com.edlplan.framework.database.v2.struct;

import java.lang.reflect.Field;

public class DBStruct {



    public static class Column {

        public String name;

        public Class valueType;

        public Field field;

    }
}
