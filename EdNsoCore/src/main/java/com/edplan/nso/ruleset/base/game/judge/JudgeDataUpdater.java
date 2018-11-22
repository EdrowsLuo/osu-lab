package com.edplan.nso.ruleset.base.game.judge;

import java.io.DataOutputStream;

/**
 * 处理原始输入数据的类
 */
public abstract class JudgeDataUpdater implements RawInputHandler{

    /**
     * 根据输入数据处理更新，当返回true时表示发生了事件，写入数据
     * 否则表示没有事件发生，不写入任何数据
     * @param outputStream 要把数据写入的类
     * @return
     */
    public abstract boolean update(DataOutputStream outputStream);

}
