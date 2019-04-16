package com.edplan.framework.database.v2.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.edplan.framework.database.v2.DBLine;
import com.edplan.framework.database.v2.DBResult;
import com.edplan.framework.database.v2.ISimpleDB;
import com.edplan.framework.database.v2.SimpleSQLException;
import com.edplan.framework.utils.functionality.Value;

import java.util.HashMap;
import java.util.Iterator;

public class SimpleSQLite implements ISimpleDB {

    SQLiteDatabase database;

    @Override
    public void open(String path) throws SimpleSQLException {
        try {
            database = SQLiteDatabase.openOrCreateDatabase(path, null);
        } catch (SQLiteException e) {
            throw new SimpleSQLException("sqlite err", e);
        }
    }

    @Override
    public void exec(String sql, String... args) throws SimpleSQLException {
        if (database == null) {
            throw new SimpleSQLException("database not opened");
        }
        database.execSQL(sql, args);
    }

    @Override
    public DBResult query(String sql, String... args) throws SimpleSQLException {
        if (database == null) {
            throw new SimpleSQLException("database not opened");
        }
        Cursor cursor = database.rawQuery(sql, args);
        HashMap<String, Integer> nameIdxMap = new HashMap<>();
        for (String key : cursor.getColumnNames()) {
            nameIdxMap.put(key, cursor.getColumnIndex(key));
        }
        return () -> new Iterator<DBLine>() {

            @Override
            public boolean hasNext() {
                return !cursor.isLast();
            }

            @Override
            public DBLine next() {
                cursor.moveToNext();
                return new DBLine() {
                    @Override
                    public String getString(String key) {
                        return cursor.getString(nameIdxMap.get(key));
                    }

                    @Override
                    public int getInt(String key) {
                        return cursor.getInt(nameIdxMap.get(key));
                    }

                    @Override
                    public long getLong(String key) {
                        return cursor.getLong(nameIdxMap.get(key));
                    }

                    @Override
                    public float getFloat(String key) {
                        return cursor.getFloat(nameIdxMap.get(key));
                    }

                    @Override
                    public float getFloat(int idx) {
                        return cursor.getFloat(idx);
                    }

                    @Override
                    public int getInt(int idx) {
                        return cursor.getInt(idx);
                    }

                    @Override
                    public long getLong(int idx) {
                        return cursor.getLong(idx);
                    }

                    @Override
                    public String getString(int idx) {
                        return cursor.getString(idx);
                    }

                    @Override
                    public ValueType getType(int idx) {
                        return toValueType(cursor.getType(idx));
                    }

                    @Override
                    public ValueType getType(String key) {
                        return toValueType(cursor.getType(nameIdxMap.get(key)));
                    }

                    public ValueType toValueType(int type) {
                        switch (type) {
                            case Cursor.FIELD_TYPE_NULL:
                                return ValueType.NULL;
                            case Cursor.FIELD_TYPE_FLOAT:
                                return ValueType.FLOAT;
                            case Cursor.FIELD_TYPE_STRING:
                                return ValueType.STRING;
                            case Cursor.FIELD_TYPE_BLOB:
                                return ValueType.BLOB;
                                default:
                                    return null;
                        }
                    }
                };
            }
        };
    }

}
