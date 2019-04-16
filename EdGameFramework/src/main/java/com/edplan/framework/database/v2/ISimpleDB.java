package com.edplan.framework.database.v2;

import com.edplan.framework.utils.ArrayUtils;
import com.edplan.framework.utils.Iterators;
import com.edplan.framework.utils.StringUtil;

import java.util.Iterator;

public interface ISimpleDB {

    void open(String path) throws SimpleSQLException;

    DBResult query(String sql, String... args) throws SimpleSQLException;

    void exec(String sql, String... args) throws SimpleSQLException;

    default Iterable<DBLine> selectAll(String tableName) throws SimpleSQLException {
        return query("SELECT * FROM " + tableName);
    }

    default Iterable<DBLine> selectAll(String tableName,String where) throws SimpleSQLException {
        return query("SELECT * FROM " + tableName + " WHERE " + where);
    }

    default <T> T selectOne(String tableName, Class<T> klass) throws SimpleSQLException {
        return SimpleDB.reflect(query("SELECT * FROM " + tableName).asOneLine(), klass);
    }

    default  <T> T selectOne(String tableName,String where, Class<T> klass) throws SimpleSQLException {
        return SimpleDB.reflect(query("SELECT * FROM " + tableName + " WHERE " + where).asOneLine(), klass);
    }

    default int count(String tableName) throws SimpleSQLException {
        return query("SELECT COUNT(*) FROM " + tableName).asOneLine().asInt();
    }

    default int count(String tableName, String where) throws SimpleSQLException {
        return query("SELECT COUNT(*) FROM " + tableName + " WHERE " + where).asOneLine().asInt();
    }

    default SimpleTable getTable(String tableName) {
        return new SimpleTable(this, tableName);
    }

    class SimpleTable{

        private String name;

        private ISimpleDB db;

        public SimpleTable(ISimpleDB db, String name) {
            this.name = name;
            this.db = db;
        }

        public Iterable<DBLine> selectAll() throws SimpleSQLException {
            return db.query("SELECT * FROM " + name);
        }

        public Iterable<DBLine> selectAll(String where) throws SimpleSQLException {
            return db.query("SELECT * FROM " + name + " WHERE " + where);
        }

        public <T> T selectOne(Class<T> klass) throws SimpleSQLException {
            return SimpleDB.reflect(db.query("SELECT * FROM " + name).asOneLine(), klass);
        }

        public <T> T selectOne(String where, Class<T> klass) throws SimpleSQLException {
            return SimpleDB.reflect(db.query("SELECT * FROM " + name + " WHERE " + where).asOneLine(), klass);
        }

        public int count() throws SimpleSQLException {
            return db.query("SELECT COUNT(*) FROM " + name).asOneLine().asInt();
        }

        public int count(String where) throws SimpleSQLException {
            return db.query("SELECT COUNT(*) FROM " + name + " WHERE " + where).asOneLine().asInt();
        }

        public class Updater {

            private String[] names;

            private Object[] values;

            public Updater names(String... names) {
                this.names = names;
                return this;
            }

            public Updater values(Object... values) {
                this.values = values;
                return this;
            }

            public void submit() throws SimpleSQLException {
                if (names == null || values == null || names.length == 0 || values.length != names.length) {
                    throw new IllegalArgumentException();
                }
                String primaryKey = names[0];
                String[] stringValues = ArrayUtils.reflect(values, this::valueToString, new String[values.length]);
                if (count(primaryKey + " = " + stringValues[0]) == 0) {
                    //Insert
                    db.exec("INSERT INTO " + name + " (" + StringUtil.link(", ", names) + ") " +
                            "values (" + StringUtil.link(", ", stringValues) + ")");
                } else {
                    //Update
                    db.exec("UPDATE " + names +
                            " SET " + StringUtil.link(" ", Iterators.join(
                            Iterators.iterator(names, 1),
                            Iterators.iterator(values, 1),
                            (n, v) -> n + " = " + v)) +
                            " WHERE " + primaryKey + " = " + stringValues[0]);
                }
            }

            private String valueToString(Object o) {
                if (o instanceof String) {
                    return "'" + o + "'";
                } else {
                    return o.toString();
                }
            }

        }
    }
}
