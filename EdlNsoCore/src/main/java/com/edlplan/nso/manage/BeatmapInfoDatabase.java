package com.edlplan.nso.manage;

import com.edlplan.framework.database.v2.SimpleDBHelper;
import com.edlplan.framework.database.v2.SimpleSQLException;

public class BeatmapInfoDatabase extends SimpleDBHelper {

    public static final int VERSION = 1;

    private static final String generalInfoTable = "general_info_table";

    BeatmapInfoDatabase(String path) {
        super(path, VERSION);
    }

    @Override
    protected void onDatabaseCreate() throws SimpleSQLException {
        super.onDatabaseCreate();
        getSimpleDB().exec("CREATE TABLE " + generalInfoTable +" (" +
                "path VARCHAR(128) PRIMARY KEY NOT NULL," +
                "audioFileName VARCHAR(128) NOT NULL," +
                "" +
                ")");
    }
}
