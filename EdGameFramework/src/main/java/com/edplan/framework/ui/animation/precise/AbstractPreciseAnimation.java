package com.edplan.framework.ui.animation.precise;

import com.edplan.framework.ui.animation.AbstractAnimation;
import com.edplan.framework.ui.animation.LoopType;

/**
 * 在PreciseTimeline上使用的动画，
 * 适应音游环境下对帧与帧之间误差的调整
 * 默认只能为LoopType.None
 */
public abstract class AbstractPreciseAnimation extends AbstractAnimation {
    private boolean hasStart = false;

    public boolean hasStart() {
        return hasStart;
    }

    public double getEndTime() {
        return getStartTimeAtTimeline() + getDuration();
    }

    /**
     * 在Timeline上的开始时间，
     * 在动画实际开始的时候（对应的帧时间）会通过这个参数进行ms级别的调整
     */
    public abstract double getStartTimeAtTimeline();

    /**
     * 默认只能为LoopType.None
     */
    @Override
    public LoopType getLoopType() {

        return LoopType.None;
    }

    @Override
    public int getLoopCount() {

        return 0;
    }

    @Override
    public void addLoopCount() {

    }

    @Override
    public void onStart() {

        hasStart = true;
    }
}
