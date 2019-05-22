package com.edlplan.framework.ui.widget;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.Anchor;
import com.edlplan.framework.ui.EdBufferedContainer;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;

public class RoundedButton extends RelativeContainer{

    public static float DEFAULT_HEIGHT = ViewConfiguration.dp(17);

    private Color4 accentColor = Color4.rgb255(0, 162, 219);

    private EdBufferedContainer.RoundedLayerPoster poster;

    private float scale = 1;

    private EdView.OnClickListener onClickListener;

    public RoundedButton(MContext c) {
        super(c);
        setClickable(true);
        setBackgroundColor(accentColor);
        poster = new EdBufferedContainer.RoundedLayerPoster(c);
        poster.setAnchor(Anchor.Center);
        poster.setRoundedRadius(ViewConfiguration.dp(4));
        setLayerPoster(poster);
    }

    private void onPressAnim() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<>(this::setScale)
                .transform(getScale(), 0, Easing.None)
                .transform(0.9f, 60, Easing.InQuad));
        ComplexAnimation anim = builder.build();
        anim.start();
        setAnimation(anim);
    }

    private void offPressAnim() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<>(this::setScale)
                .transform(getScale(), 0, Easing.None)
                .transform(1, 60, Easing.OutQuad));
        ComplexAnimation anim = builder.build();
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

    public void setOnClickListener(EdView.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public EdView.OnClickListener getOnClickListener() {
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
