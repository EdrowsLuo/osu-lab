package com.edplan.osulab.ui.scenes.songs;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.component.Hideable;

public class BottomBar extends RelativeLayout implements Hideable {
    public static float HEIGHT_DP = 40;

    public BottomBar(MContext c) {
        super(c);
        setBackgroundColor(Color4.rgba(0, 0, 0, 0.6f));
    }


    @Override
    public void hide() {

        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<BottomBar>(this, "offsetY")
                .transform(getOffsetY(), 0, Easing.None)
                .transform(getHeight(), ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutExpo));
        ComplexAnimation anim = builder.build();
        anim.start();
        setAnimation(anim);
    }

    @Override
    public void show() {

        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<BottomBar>(this, "offsetY")
                .transform(getHeight(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutExpo));
        ComplexAnimation anim = builder.build();
        anim.start();
        setAnimation(anim);
    }

    @Override
    public boolean isHidden() {

        return getVisiblility() == VISIBILITY_GONE;
    }
}
