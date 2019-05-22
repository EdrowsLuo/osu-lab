package com.edlplan.framework.database.v2;

import java.io.File;
import java.io.IOException;

public class SimpleDBHelper {

    private static String __helper_properties = "__helper_properties";

    private String path;

    private ISimpleDB simpleDB;

    private ISimpleDB.SimpleTable helperPropertyTable;

    private int version;

    public SimpleDBHelper(String path, int version) {
        this.path = path;
        this.version = version;
        simpleDB = SimpleDB.instance();
    }

    public int getVersion() {
        return version;
    }

    public ISimpleDB getSimpleDB() {
        return simpleDB;
    }

    public void open() throws SimpleSQLException, IOException {
        File file = new File(path);
        if (file.exists()) {
            simpleDB.open(path);
            int dbVersion = queryDatabaseVersion();
            if (dbVersion > getVersion()) {
                throw new SimpleSQLException("db version is bigger than client version");
            }
            if (dbVersion < getVersion()) {
                databaseUpgrade(dbVersion,getVersion());
            }
        } else {
            file.getParentFile().mkdirs();
            file.createNewFile();
            simpleDB.open(path);
            onDatabaseCreate();
        }
    }

    public int queryDatabaseVersion() throws SimpleSQLException {
        return simpleDB.query(
                "SELECT value FROM " + __helper_properties + " " +
                        "WHERE key = 'version'")
                .asOneLine().asInt();
    }

    /**
     * Called when the database is created, you can exec your create table sentence here
     * @throws SimpleSQLException
     */
    protected void onDatabaseCreate() throws SimpleSQLException {
        simpleDB.exec("CREATE TABLE " + __helper_properties + " (" +
                "key varchar(128) PRIMARY KEY NOT NULL," +
                "value varchar(128) NOT NULL" +
                ")");
        simpleDB.exec("INSERT INTO " + __helper_properties + " (key, value) " +
                "VALUES ('version','" + getVersion() + "')");
    }

    /**
     * Called when the database upgrade from oldVersion to newVersion
     * @param oldVersion
     * @param newVersion
     */
    protected void onDatabaseUpgrade(int oldVersion, int newVersion) {

    }

    private void databaseUpgrade(int oldVersion, int newVersion) throws SimpleSQLException {
        try {
            onDatabaseUpgrade(oldVersion, newVersion);
        } catch (Exception e) {
            throw new SimpleSQLException("err upgrade db", e);
        }
        simpleDB.exec("UPDATE " + __helper_properties + " " +
                "SET value = '" + getVersion() + "' " +
                "WHERE key = 'version'"
        );
    }
}
