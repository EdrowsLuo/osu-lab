package com.edplan.nso.ruleset.base.beatmap;

/**
 * 对铺面的最基础描述
 */
public class BeatmapDescription {

    /**
     * 原始文件路径
     */
    public String filePath;

    /**
     * 原始文件对应的模式
     */
    public String beatmapType;

    /**
     * 缓存的预加载beatmap数据，可以为空
     */
    public Beatmap cachedBeatmap;
}
