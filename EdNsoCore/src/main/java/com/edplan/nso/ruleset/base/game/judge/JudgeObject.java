package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;

public abstract class JudgeObject {


    private boolean release = false;

    /**
     * 当这个物件被释放时被调用
     */
    protected void onRelease() {

    }

    /**
     * 物件处理完毕，释放物件
     */
    public final void releaseObject(){
        if (release) {
            return;
        }
        release = true;
        onRelease();
    }

    public final boolean isRelease() {
        return release;
    }

    /**
     * @return 开始进入判定队列的时间
     */
    public abstract double getStartJudgeTime();

    /**
     * @return 当超过这个时间还没有release会强制release
     */
    public abstract double getJudgeFailedTime();

    /**
     * 按照注册的JudgeData顺序调用，当没有任何JudgeData被注册
     * 或者getListeningDatas包含null的时候会在处理任何事件处理前按data为null调用一次
     * @param data 对应的数据
     * @param world 上下文世界
     * @return 是否阻塞处理，阻塞处理的话对应事件不会下发到其他物件
     */
    public abstract boolean handle(JudgeData data, int subType, World world);

    /**
     * @return 申请的JudgeData类型，用于注册监听器（不许有重复的类）
     */
    public abstract Class<? extends JudgeData>[] getListeningData();

    public int[] getListeningDataSubTypes() {
        return null;
    }
}
