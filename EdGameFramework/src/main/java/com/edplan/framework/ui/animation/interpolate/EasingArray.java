package com.edplan.framework.ui.animation.interpolate;

import com.edplan.framework.ui.animation.Easing;

public class EasingArray {

    private static final EasingInterpolator[] easings = new EasingInterpolator[Easing.values().length];

    static {
        for (int i = 0; i < easings.length; i++) {
            Easing easing = Easing.getEasing(i);
            easings[i] = v -> (float) EasingManager.apply(easing, v);
        }
        easings[Easing.None.id] = v -> v;
        easings[Easing.In.id] = easings[Easing.InQuad.id] = v -> v * v;
    }

}
