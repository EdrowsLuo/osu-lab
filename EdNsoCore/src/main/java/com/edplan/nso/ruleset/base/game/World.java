package com.edplan.nso.ruleset.base.game;

import com.edplan.framework.math.RectF;

/**
 * 世界相关的数据和操作
 */
public abstract class World {

    /** 世界窗口位置 **/
    private RectF worldRect;

    /** 判定世界 **/
    private JudgeWorld judgeWorld;

    /** 绘制世界 **/
    private PaintWorld paintWorld;

    /**
     * @return 世界的实时时间（不是帧时间）
     */
    public abstract double getWorldTime();

    public abstract double getPaintFrameTime();

    public abstract double getJudgeFrameTime();

}
