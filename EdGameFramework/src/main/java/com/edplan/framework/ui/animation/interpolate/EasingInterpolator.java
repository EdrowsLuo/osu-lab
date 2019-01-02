package com.edplan.framework.ui.animation.interpolate;

@FunctionalInterface
public interface EasingInterpolator {

    float interpolate(float v);

    default float apply(float start, float end, float progress) {
        return start + (end - start) * interpolate(progress);
    }

}
