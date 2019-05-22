package com.edlplan.framework.ui.animation;

import com.edlplan.framework.utils.MLog;
import com.edlplan.framework.utils.SafeList;
import com.edlplan.framework.timing.Loopable;

import java.util.Iterator;

public class AnimationHandler extends Loopable {
    private SafeList<AbstractAnimation> animations = new SafeList<AbstractAnimation>();

    public void addAnimation(AbstractAnimation anim) {
        animations.add(anim);
    }

    @Override
    public void onLoop(double deltaTime) {

        animations.startIterate();
        Iterator<AbstractAnimation> iter = animations.iterator();
        AbstractAnimation anim;
        while (iter.hasNext()) {
            anim = iter.next();
            if (handleSingleAnima(anim, deltaTime)) {
                iter.remove();
            }
        }
        animations.endIterate();
    }

    public static boolean handleSingleAnima(AbstractAnimation anim, double deltaTime) {
        switch (anim.getState()) {
            case Waiting:
                return false;
            case Skip:
                skipAnimation(anim);
                onRemoveAnimation(anim);
                return true;
            case Stop:
                stopAnimation(anim);
                onRemoveAnimation(anim);
                return true;
            case Running:
                if (postProgress(anim, deltaTime)) {
                    finishAnimation(anim);
                    onRemoveAnimation(anim);
                    return true;
                }
                return false;
            default:
                MLog.test.vOnce("switch-err", "???", "什么鬼啊");
                return false;
        }
    }

    /**
     * 返回值代表此动画是否Finish
     */
    private static boolean postProgress(AbstractAnimation anim, double postTime) {
        double p;
        switch (anim.getLoopType()) {
            case None:
                p = anim.getProgressTime() + postTime;
                if (p < anim.getDuration()) {
                    anim.postProgressTime(postTime);
                    anim.onProgress(p);
                } else {
                    p = anim.getDuration();
                    anim.setProgressTime(p);
                    anim.onProgress(p);
                    return true;
                }
                break;
            case Loop:
                p = anim.getProgressTime() + postTime;
                if (p < anim.getDuration()) {
                    anim.postProgressTime(postTime);
                    anim.onProgress(p);
                } else {
                    p %= anim.getDuration();
                    anim.setProgressTime(p);
                    anim.addLoopCount();
                    anim.onProgress(p);
                }
                break;
            case LoopAndReverse:
                //没有进行特别的参数检查，请确保各参数在范围中
                if (anim.getLoopCount() % 2 == 0) {
                    p = anim.getProgressTime() + postTime;
                    if (p < anim.getDuration()) {
                        anim.setProgressTime(p);
                        anim.onProgress(p);
                    } else {
                        p = anim.getDuration() - p % anim.getDuration();
                        anim.setProgressTime(p);
                        anim.addLoopCount();
                        anim.onProgress(p);
                    }
                } else {
                    p = anim.getProgressTime() - postTime;
                    if (p > 0) {
                        anim.setProgressTime(p);
                    } else {
                        p = -p;
                        anim.setProgressTime(p);
                        anim.addLoopCount();
                        anim.onProgress(p);
                    }
                }
                break;
            case Endless:
                anim.postProgressTime(postTime);
                break;
            default:
                MLog.test.vOnce("err-anim-postProgress", "?--?", "谁让你走到这里的？？( ✘_✘ )↯");
        }
        return false;
    }

    private static void finishAnimation(AbstractAnimation anim) {
        anim.onFinish();
    }

    private static void skipAnimation(AbstractAnimation anim) {
        anim.setProgressTime(anim.getDuration());
    }

    private static void stopAnimation(AbstractAnimation anim) {

    }

    private static void onRemoveAnimation(AbstractAnimation anim) {
        anim.onEnd();
    }
}
