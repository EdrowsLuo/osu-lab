package com.edplan.nso.ruleset.base.game.judge;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
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
     * @param inputStream 读取数据的流，自动插帧时会传入null
     */
    public abstract void update(int type, DataInput inputStream);

    public abstract JudgeDataUpdater getDataUpdater();

}
