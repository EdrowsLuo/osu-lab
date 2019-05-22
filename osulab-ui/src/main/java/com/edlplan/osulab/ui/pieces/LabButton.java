package com.edlplan.osulab.ui.pieces;

import com.edlplan.osulab.ui.UiConfig;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.Anchor;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.widget.RelativeContainer;

public class LabButton extends RelativeContainer {
    public static float DEFAULT_HEIGHT = ViewConfiguration.dp(17);

    private Color4 accentColor = UiConfig.Color.BLUE.copyNew();

    private RoundedLayerPoster poster;

    private float scale = 1;

    private OnClickListener onClickListener;

    public LabButton(MContext c) {
        super(c);
        setClickable(true);
        setBackgroundColor(accentColor);
        poster = new RoundedLayerPoster(c);
        poster.setAnchor(Anchor.Center);
        poster.setRoundedRadius(ViewConfiguration.dp(4));
        //poster.setShadow(ViewConfiguration.dp(2),Color4.rgba(1,1,1,0.3f),Color4.rgba(0,0,0,0));
        setLayerPoster(poster);
    }

    private void onPressAnim() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<LabButton>(this, "scale")
                .transform(getScale(), 0, Easing.None)
                .transform(0.9f, 60, Easing.InQuad));
        ComplexAnimation anim = builder.build();
        //anim.setOnProgressListener(invalideDrawDuringAnimation());
        anim.start();
        setAnimation(anim);
    }

    private void offPressAnim() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<LabButton>(this, "scale")
                .transform(getScale(), 0, Easing.None)
                .transform(1, 60, Easing.OutQuad));
        ComplexAnimation anim = builder.build();
        //anim.setOnProgressListener(invalideDrawDuringAnimation());
        anim.start();
        setAnimation(anim);
    }

    @Override
    public void setPressed(boolean pressed) {

        super.setPressed(pressed);
        if (pressed) {
            onPressAnim();
        } else {
            offPressAnim();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void onClickEvent() {

        super.onClickEvent();
        if (onClickListener != null) onClickListener.onClick(this);
    }

    public void setScale(float scale) {
        this.scale = scale;
        poster.setScale(scale);
        invalidateDraw();
    }

    public float getScale() {
        return scale;
    }

    public void setRoundedRadius(float r) {
        poster.setRoundedRadius(r);
    }

    public float getRoundedRadius() {
        return poster.getRoundedRadius();
    }
}
