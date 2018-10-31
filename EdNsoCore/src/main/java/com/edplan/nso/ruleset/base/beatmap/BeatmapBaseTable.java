package com.edplan.nso.ruleset.base.beatmap;

import com.edplan.framework.database.v2.DatabaseTable;
import com.edplan.framework.utils.Lazy;

public class BeatmapBaseTable extends DatabaseTable {

    /**
     * 在数据库里的id，用于Ruleset扩展用（每一个新的图被载入都会生成一个新的id）
     */
    @Index(0)
    @Type(DataType.INTEGER)
    @Extra(Extra.PKA)
    public Row dbID;

    /**
     * 文件相对路径（相对于Songs目录）
     */
    @Index(1)
    @Type(DataType.TEXT)
    public Row filePath;

    /**
     * 文件的hash值，用来判断文件是否发生更改
     */
    @Index(2)
    @Type(DataType.TEXT)
    public Row md5hash;

    /**
     * 原始Ruleset的名称
     */
    @Index(3)
    @Type(DataType.TEXT)
    public Row rawRulesetName;


    /**
     * 一些信息
     */
    @Index(4)
    @Type(DataType.INTEGER)
    public Row beatmapSetId;

    @Index(5)
    @Type(DataType.INTEGER)
    public Row beatmapId;

    @Index(6)
    @Type(DataType.TEXT)
    public Row artist;

    @Index(7)
    @Type(DataType.TEXT)
    public Row creater;

    @Index(8)
    @Type(DataType.TEXT)
    public Row title;

    @Index(9)
    @Type(DataType.TEXT)
    public Row version;

    @Index(10)
    @Type(DataType.TEXT)
    public Row source;

    /**
     * 四围信息
     */
    @Index(110)
    @Type(DataType.REAL)
    public Row hp;
    @Index(111)
    @Type(DataType.REAL)
    public Row cs;
    @Index(112)
    @Type(DataType.REAL)
    public Row od;
    @Index(113)
    @Type(DataType.REAL)
    public Row ar;

    @Index(200)
    @Type(DataType.TEXT)
    public Row audioFile;

    @Index(201)
    @Type(DataType.TEXT)
    public Row backgroundFile;

    @Index(202)
    @Type(DataType.REAL)
    public Row bpmMin;

    @Index(203)
    @Type(DataType.REAL)
    public Row bpmMax;

    @Index(204)
    @Type(DataType.REAL)
    public Row starRate;

    @Index(205)
    @Type(DataType.INTEGER)
    public Row duration;

    @Index(304)
    @Type(DataType.INTEGER)
    public Row dateAdd;

    @Index(305)
    @Type(DataType.INTEGER)
    public Row dateUpdate;

    @Index(306)
    @Type(DataType.INTEGER)
    public Row datePlayed;

    @Index(307)
    @Type(DataType.INTEGER)
    public Row playedCount;


    public final Lazy<DatabaseOperation> insertAll = new Lazy<DatabaseOperation>() {
        @Override
        protected DatabaseOperation initial() {
            return new DatabaseOperation(insertSQL());
        }
    };

    @Override
    protected void onLoad() {
        checkAndCreateTable();
    }
}
