package com.edlplan.framework.timing;

import com.edlplan.framework.ui.animation.AnimState;
import com.edlplan.framework.ui.animation.precise.AbstractPreciseAnimation;
import com.edlplan.framework.utils.MLog;
import com.edlplan.framework.utils.SafeList;

import java.util.Iterator;

public class PreciseTimeline extends Loopable {
    private double latestFrameTime = 0;

    private double preFrameDeltaTime;

    private boolean pausing = false;

    private boolean askPause = false;

    private boolean hasRestart = true;

    private SafeList<AbstractPreciseAnimation> animations = new SafeList<AbstractPreciseAnimation>();

    public PreciseTimeline() {

    }

    public PreciseTimeline(double offset) {
        latestFrameTime = offset;
    }

    public int animationCount() {
        return animations.size();
    }

    public void addAnimation(AbstractPreciseAnimation a) {
        animations.add(a);
    }

    @Override
    public void onLoop(double deltaTime) {

        if (pausing) {

        } else {
            if (askPause) {
                //接受暂停请求时应该将当前帧处理完
                handleFrame(deltaTime);
                pausing = true;
                hasRestart = false;
                onPause();
            } else if (hasRestart) {
                handleFrame(deltaTime);
            } else {
                hasRestart = true;
                onRestart();
                handleFrame(deltaTime);
            }
        }
    }

    protected void handleFrame(double deltaTime) {
        this.preFrameDeltaTime = deltaTime;
        latestFrameTime += deltaTime;
        handlerAnimation(deltaTime);
    }

    protected void handlerAnimation(double deltaTime) {
        animations.startIterate();
        Iterator<AbstractPreciseAnimation> iter = animations.iterator();
        AbstractPreciseAnimation anim;
        while (iter.hasNext()) {
            anim = iter.next();
            switch (anim.getState()) {
                case Waiting:
                    break;
                case Skip:
                    skipAnimation(anim);
                    onRemoveAnimation(anim);
                    iter.remove();
                    break;
                case Stop:
                    stopAnimation(anim);
                    onRemoveAnimation(anim);
                    iter.remove();
                    break;
                case Running:
                    if (postProgress(anim, deltaTime)) {
                        finishAnimation(anim);
                        onRemoveAnimation(anim);
                        iter.remove();
                    }
                    break;
                default:
                    MLog.test.vOnce("switch-err", "???", "什么鬼啊");
            }
        }
        animations.endIterate();
    }

    /**
     * 返回值代表此动画是否Finish
     */
    private boolean postProgress(AbstractPreciseAnimation anim, double postTime) {
        double p;
        p = frameTime() - anim.getStartTimeAtTimeline();
        if (p >= 0) {
            if (p < anim.getDuration()) {
                if (!anim.hasStart()) anim.onStart();
                anim.postProgressTime(p - anim.getProgressTime());
                anim.onProgress(p);
                return false;
            } else {
                p = anim.getDuration();
                if (!anim.hasStart()) anim.onStart();
                anim.postProgressTime(p - anim.getProgressTime());
                anim.onProgress(p);
                return true;
            }
        } else return false;
    }

    private void finishAnimation(AbstractPreciseAnimation anim) {
        anim.onFinish();
        anim.dispos();
    }

    private void skipAnimation(AbstractPreciseAnimation anim) {
        anim.setProgressTime(anim.getDuration());
    }

    private void stopAnimation(AbstractPreciseAnimation anim) {

    }

    private void onRemoveAnimation(AbstractPreciseAnimation anim) {
        anim.onEnd();
    }


    /**
     * 暂停Timeline，不会立即暂停，会等到接受下一桢时才处理
     */
    public void pause() {
        askPause = true;
    }

    /**
     * 暂停后的重启，同样不会立即执行
     */
    public void restart() {
        pausing = false;
    }

    /**
     * 触发暂停时的回调
     */
    protected void onPause() {

    }

    /**
     * 被暂停又被恢复后，第一次进入新一帧的时候被调用
     */
    protected void onRestart() {

    }

    /**
     * 当前帧刷新时的时间
     */
    public double frameTime() {
        return latestFrameTime;
    }

    /**
     * 上一帧到这一帧的时间差
     */
    public double deltaTime() {
        return preFrameDeltaTime;
    }
}
