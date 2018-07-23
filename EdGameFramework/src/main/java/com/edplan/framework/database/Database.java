package com.edplan.framework.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.io.File;

public class Database {
    private SQLiteDatabase database;

    private HashMap<Class<? extends DatabaseLine>, DatabaseTable> tables = new HashMap<Class<? extends DatabaseLine>, DatabaseTable>();

    public Database(SQLiteDatabase db) {
        this.database = db;
    }

    public Database(File file) {
        database = SQLiteDatabase.openOrCreateDatabase(file, null);
    }

    public DatabaseTable initialTable(Class<? extends DatabaseLine> line,String tableName) {
        if (tables.containsKey(line)) {
            return tables.get(line);
        } else {
            DatabaseTable table = new DatabaseTable(database);
            table.initial(line,tableName);
            tables.put(line, table);
            return table;
        }
    }

    public DatabaseTable getTable(Class<? extends DatabaseLine> line) {
        return tables.get(line);
    }
}
