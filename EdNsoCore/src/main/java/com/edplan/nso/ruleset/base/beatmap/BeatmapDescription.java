package com.edplan.nso.ruleset.base.beatmap;

import com.edplan.framework.resource.AResource;
import com.edplan.framework.resource.DirResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对铺面的最基础描述
 */
public class BeatmapDescription {

    /**
     * 原始文件路径
     */
    private String filePath;

    public AResource openDirRes() {
        return new DirResource(new File(filePath).getParentFile());
    }

    public InputStream openBeatmapStream() throws IOException {
        return new FileInputStream(filePath);
    }

    /**
     * 原始文件对应的模式
     */
    public String beatmapType;

    /**
     * 缓存的预加载beatmap数据，可以为空
     */
    public Beatmap cachedBeatmap;
}
