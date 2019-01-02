package com.edplan.nso.ruleset.base.game.paint;

/**
 * 将动画的预处理全部在build时进行，动画运行时只会进行最少的操作来保证效率
 */
public class AnimationBuilder {

    private IAnimateObject animate;

    private float time;

    public AnimationBuilder(IAnimateObject animate) {

    }


}
