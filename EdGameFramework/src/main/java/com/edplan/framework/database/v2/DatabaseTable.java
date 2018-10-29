package com.edplan.framework.database.v2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edplan.framework.utils.AdvancedStringBuilder;
import com.edplan.framework.utils.Function;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatabaseTable {

    private static final int NOT_PREPARE=0,IN_PREPARE=1, PREPARED = 3;

    private List<Row> structs;

    protected SQLiteDatabase database;

    private String tablename;

    public DatabaseTable() {

    }

    public final void load(SQLiteDatabase database) {
        if (this.database != null) {
            return;
        }
        loadAnnotation();
        this.database = database;
        onLoad();
    }

    private void loadAnnotation() {
        try {
            structs = new ArrayList<>();
            Class klass = this.getClass();
            tablename = ((TableName) klass.getAnnotation(TableName.class)).value();
            Field[] fields = klass.getFields();
            for (Field f : fields) {
                if (f.getType().equals(Row.class) && f.isAnnotationPresent(Index.class)) {
                    String name = f.isAnnotationPresent(Name.class) ?
                            f.getAnnotation(Name.class).value() : f.getName();
                    if (Name.AUTO.equals(name)) {
                        name = f.getName();
                    }
                    DataType dataType = f.getAnnotation(Type.class).value();
                    String extra = f.isAnnotationPresent(Extra.class) ? f.getAnnotation(Extra.class).value() : null;
                    int index = f.getAnnotation(Index.class).value();
                    f.setAccessible(true);
                    Row row = new Row(index, name, dataType, extra);
                    f.set(this, row);
                    structs.add(row);
                }
            }
            Collections.sort(structs, new Comparator<Row>() {
                @Override
                public int compare(Row o1, Row o2) {
                    return o1.index - o2.index;
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("reflect err illaccs: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("reflect err illagr: " + e.getMessage(), e);
        }
    }

    private void checkAndCreateTable() {
        database.execSQL(createTableSQL());
    }

    protected String createTableSQL() {
        AdvancedStringBuilder sb = new AdvancedStringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tablename).append(" (");
        sb.append(structs, ",", new Function<Row, String>() {
            @Override
            public String reflect(Row value) {
                return value.toSqlEntry();
            }
        }).append(")");
        return sb.toString();
    }

    protected String selectAllSQL() {
        return String.format("SELECT * FROM %s", tablename);
    }


    /**
     * @param data 一个row一个match方式相对应
     * @return 对应的SQL语句
     */
    protected String selectAllMatchSQL(Object[][] data) {
        AdvancedStringBuilder stringBuilder = new AdvancedStringBuilder();
        stringBuilder.append("SELECT * FROM ").append(tablename).append(" WHERE ")
                .append(Arrays.asList(data), " AND ", new Function<Object[], String>() {
                    @Override
                    public String reflect(Object[] value) {
                        return String.format("%s %s ?", ((Row) value[0]).name, value[1]);
                    }
                });
        return stringBuilder.toString();
    }

    protected String insertSQL() {
        AdvancedStringBuilder stringBuilder = new AdvancedStringBuilder();
        stringBuilder.append("INSERT INTO ").append(tablename).append(" VALUES (")
                .appendRepeat("?", ", ", structs.size());
        return stringBuilder.toString();
    }

    protected void onLoad() {
        checkAndCreateTable();
    }

    public Cursor rawqurey(String sql, String... data) {
        return database.rawQuery(sql, data);
    }



    public static class Row {
        public final int index;
        public final String name;
        public final DataType type;
        public final String extra;

        public Row(int index,String name, DataType type, String extra) {
            this.index = index;
            this.name = name;
            this.type = type;
            this.extra = null;
        }

        public String toSqlEntry() {
            return String.format("%s %s", name, type.name()) + ((extra != null) ? (" " + extra) : "");
        }
    }

    public enum DataType {
        INTEGER,
        TEXT,
        REAL
    }

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Type {
        DataType value();
    }


    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String AUTO = "auto_set";
        String value() default AUTO;
    }

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Index {
        int value();
    }

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Extra{
        String PKA = "PRIMARY KEY AUTOINCREMENT";
        String value();
    }

    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TableName{
        String value();
    }

}
