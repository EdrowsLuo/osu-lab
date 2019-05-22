package com.edlplan.framework.ui.animation;

import com.edlplan.framework.utils.interfaces.Consumer;

import java.util.Stack;

public class ComplexAnimationBuilder {
    private ComplexAnimation target;

    private Stack<Double> keyTimeStack = new Stack<Double>();

    private double currentKeyTime = 0;

    private AbstractAnimation keyAnimation;

    private Consumer<ComplexAnimation> onBuild;

    public ComplexAnimationBuilder() {
        target = new ComplexAnimation();
    }

    protected void onBuild(Consumer<ComplexAnimation> onBuild) {
        this.onBuild = onBuild;
    }

    /**
     * 进入分割队列，单独计算关键帧时间
     */
    public ComplexAnimationBuilder beginQuery() {
        keyTimeStack.push(currentKeyTime);
        return this;
    }

    public ComplexAnimationBuilder endQuery() {
        currentKeyTime = keyTimeStack.pop();
        return this;
    }

    protected void startAnim(AbstractAnimation anim) {
        keyAnimation = anim;
        target.addAnimation(anim, currentKeyTime);
    }

    //设置第一个动画，并以此动画开始时间为关键帧
    public static ComplexAnimationBuilder start(AbstractAnimation anim) {
        ComplexAnimationBuilder builder = new ComplexAnimationBuilder();
        builder.startAnim(anim);
        return builder;
    }

    /**
     * 添加一个和当前关键帧动画平行进行的动画，可能在进入下一关键帧的时候仍在进行
     *
     * @param offset: 与关键帧开始时间的时间差
     */
    public ComplexAnimationBuilder together(AbstractAnimation anim, double offset) {
        target.addAnimation(anim, currentKeyTime + offset);
        return this;
    }

    public ComplexAnimationBuilder together(AbstractAnimation anim) {
        target.addAnimation(anim, currentKeyTime);
        return this;
    }

    /**
     * 添加一个在当前动画结束后offset ms后开始的动画，并平移关键帧到此动画开始时间
     *
     * @param offset: 与上一关键帧结束时间的时间差
     */
    public ComplexAnimationBuilder then(AbstractAnimation anim, double offset) {
        next();
        delay(offset);
        keyAnimation = anim;
        target.addAnimation(anim, currentKeyTime);
        return this;
    }

    /**
     * 平移当前帧到当前动画结束位置
     */
    public ComplexAnimationBuilder next() {
        if (keyAnimation != null) {
            delay(keyAnimation.getDuration());
        }
        return this;
    }

    /**
     * 平移关键帧
     */
    public ComplexAnimationBuilder delay(double time) {
        currentKeyTime += time;
        keyAnimation = null;
        return this;
    }

    public ComplexAnimation build() {
        target.build();
        if (onBuild != null) {
            onBuild.consume(target);
        }
        return target;
    }

    public ComplexAnimation buildAndStart() {
        target.build();
        target.start();
        return target;
    }
}
