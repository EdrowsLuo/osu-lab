package com.edlplan.nso.ruleset.base.game.judge;

import com.edlplan.framework.timing.FrameClock;

import java.io.DataOutput;

/**
 * 处理原始输入数据的类
 */
public abstract class JudgeDataUpdater implements RawInputHandler{

    /**
     * @return 是否需要更新，保证调用之后调用update返回true
     */
    public abstract boolean needUpdate();

    /**
     * 根据输入数据处理更新，当返回true时表示发生了事件，写入数据
     * 否则表示没有事件发生，不写入任何数据
     *
     * @param outputStream 要把数据写入的类
     * @return 是否写入了事件
     */
    public abstract boolean update(DataOutput outputStream);

}
