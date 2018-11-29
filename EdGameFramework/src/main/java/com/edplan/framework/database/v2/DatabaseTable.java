package com.edplan.framework.database.v2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edplan.framework.utils.advance.AdvancedStringBuilder;
import com.edplan.framework.utils.CollectionUtil;
import com.edplan.framework.utils.interfaces.Function;
import com.edplan.framework.utils.JudgeStatement;

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

public abstract class DatabaseTable {

    private static final int NOT_PREPARE=0,IN_PREPARE=1, PREPARED = 3;

    protected static final String WHERE = "WHERE";

    protected static final String AND = "AND";

    protected static final String OR = "OR";

    protected static final String SELECT = "SELECT";

    protected static final String INSERT_INTO = "INSERT INTO";

    protected static final String LIMIT = "LIMIT";

    private List<Row> structs;

    protected SQLiteDatabase database;

    private String tablename;

    public DatabaseTable() {
        loadAnnotation();
    }

    public final void load(SQLiteDatabase database) {
        if (this.database != null) {
            return;
        }
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
                    Row row = new Row(this, index, name, dataType, extra, Extra.PKA.equals(extra));
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

    protected void checkAndCreateTable() {
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

    protected final void checkRows(List<Row> list) {
        if (list == null) {
            return;
        }
        if (CollectionUtil.oneMatch(list, new JudgeStatement<Row>() {
            @Override
            public boolean judge(Row v) {
                return v.table != DatabaseTable.this;
            }
        })) {
            throw new RuntimeException("创建列数组时请使用本类创建的Row");
        }
    }

    protected final void checkRows(Row... rows) {
        if (rows == null) {
            return;
        }
        checkRows(Arrays.asList(rows));
    }

    protected String selectSQL(Row[] rows, String... extra) {
        checkRows(rows);
        AdvancedStringBuilder stringBuilder = new AdvancedStringBuilder("SELECT ");
        if (rows == null) {
            stringBuilder.append("*");
        } else {
            stringBuilder.append("(").append(Arrays.asList(rows), ", ").append(")");
        }
        stringBuilder.append(" FROM ").append(tablename).append(" ").append(Arrays.asList(extra), " ");
        return stringBuilder.toString();
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
        final List<Row> notAuto = new ArrayList<>();
        for (Row row : structs) {
            if (!row.autoIncrement) {
                notAuto.add(row);
            }
        }
        return insertSQL(notAuto.toArray(new Row[notAuto.size()]));
    }

    protected String insertSQL(Row... rows) {
        AdvancedStringBuilder stringBuilder = new AdvancedStringBuilder();
        stringBuilder.append("INSERT INTO ").append(tablename).append(" (")
                .append(Arrays.asList(rows), ", ", new Function<Row, String>() {
                    @Override
                    public String reflect(Row value) {
                        return value.name;
                    }
                })
                .append(") VALUES (")
                .appendRepeat("?", ", ", rows.length);
        return stringBuilder.toString();
    }

    protected abstract void onLoad();

    public Cursor rawqurey(String sql, String... data) {
        return database.rawQuery(sql, data);
    }

    public void rawexec(String sql, String... data) {
        database.execSQL(sql,data);
    }

    public class DatabaseOperation {

        String sql;

        public DatabaseOperation(String sql) {
            this.sql = sql;
        }

        private String[] toStringArray(Object... datas) {
            String[] comp = new String[datas.length];
            for (int i = 0; i < datas.length; i++) {
                comp[i] = datas[i].toString();
            }
            return comp;
        }

        public void exec(Object... datas) {
            rawexec(sql, toStringArray(datas));
        }

        public Cursor qurey(Object... datas) {
            return rawqurey(sql, toStringArray(datas));
        }
    }

    public static class Row {
        public final int index;
        public final boolean autoIncrement;
        public final String name;
        public final DataType type;
        public final String extra;
        public final DatabaseTable table;

        protected Row(DatabaseTable table,int index, String name, DataType type, String extra, boolean autoIncrement) {
            this.table = table;
            this.index = index;
            this.name = name;
            this.type = type;
            this.extra = extra;
            this.autoIncrement = autoIncrement;
        }

        public String toSqlEntry() {
            return String.format("%s %s", name, type.name()) + ((extra != null) ? (" " + extra) : "");
        }

        public String judge(String way) {
            return String.format("%s %s ?", name, way);
        }

        public String sameAs() {
            return judge("=");
        }

        @Override
        public String toString() {
            return name;
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
