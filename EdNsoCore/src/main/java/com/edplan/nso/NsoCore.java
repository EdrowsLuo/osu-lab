package com.edplan.nso;

import java.util.HashMap;

import com.edplan.nso.ruleset.base.Ruleset;
import com.edplan.nso.ruleset.base.beatmap.BeatmapStorage;
import com.edplan.framework.MContext;
import com.edplan.framework.database.Database;

import java.io.File;

import android.os.Environment;

import com.edplan.nso.ruleset.base.beatmap.LowDetailBeatmap;
import com.edplan.framework.utils.FileUtils;

/**
 * 将主要游戏的内核部分分离出来。
 * <p>
 * 具体分离出来的部分有：
 * --1：Ruleset的管理
 * --2：Beatmap的解析和地图库的管理，难度的计算
 * --3：具体的游戏画面生成以及对应的接口
 * --4：Storyboard的解析绘制
 */
public class NsoCore {
    private File mainDir;

    private File databaseDir;

    private MContext context;

    private NsoConfig config;

    private HashMap<String, Ruleset> rulesets = new HashMap<String, Ruleset>();

    private HashMap<Class, Ruleset> class2ruleset = new HashMap<Class, Ruleset>();

    private Database mainDatabase;

    private Database beatmapDatabase;

    private BeatmapStorage beatmapStorage;

    public NsoCore(MContext context, NsoConfig conf) {
        this.config = (conf != null) ? conf : new NsoConfig();
        this.context = context;
        this.beatmapStorage = new BeatmapStorage(this);

        mainDir = new File(Environment.getExternalStorageDirectory(), "osu!lab");
        FileUtils.checkExistDir(mainDir);

        databaseDir = new File(mainDir, "database");
        FileUtils.checkExistDir(databaseDir);

        mainDatabase = new Database(new File(databaseDir, "main.db"));
        loadMainDatabase();

        beatmapDatabase = new Database(new File(databaseDir, "beatmap.db"));
        loadBeatmapDatabase();
    }

    protected void loadMainDatabase() {

    }

    protected void loadBeatmapDatabase() {
        //beatmapDatabase.getTable(LowDetailBeatmap.class);
    }

    public void setMainDatabase(Database mainDatabase) {
        this.mainDatabase = mainDatabase;
    }

    public Database getMainDatabase() {
        return mainDatabase;
    }

    protected void setConfig(NsoConfig config) {
        this.config = config;
    }

    public NsoConfig getConfig() {
        return config;
    }

    public void setContext(MContext context) {
        this.context = context;
    }

    public MContext getContext() {
        return context;
    }
}
