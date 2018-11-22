package com.edplan.nso.ruleset.base.game.judge;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 两种数据更新方式，一种是用关键帧数据来更新，一种是自动插帧
 */
public abstract class JudgeData {

    public static final int USING_KEYFRAME_DATA = 1;

    public static final int AUTO_CHANGE = 2;

    /**
     *  从inputStream里更新数据或者自动插帧，取决于type的值
     * @param type 决定是否是自动插帧
     * @param inputStream 读取数据的流
     */
    public abstract void update(int type, DataInputStream inputStream);

    /**
     * 将当前帧事件写入流中
     * @param outputStream
     */
    public abstract void writeToStream(DataOutputStream outputStream);

    public abstract JudgeDataUpdater getDataUpdater();

}
