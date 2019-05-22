package com.edlplan.osulab.ui.toolbar;

import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edlplan.framework.ui.drawable.sprite.TextureSprite;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.Anchor;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.ui.text.font.FontAwesome;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.callback.OnProgressListener;

public class ToolBarButton extends EdView {
    private ColorRectSprite backlight;

    private TextureSprite icon;

    private OnClickListener onClickListener;

    public ToolBarButton(MContext c) {
        super(c);
        setClickable(true);
        backlight = new ColorRectSprite(c);
        float gr = 1f;
        backlight.setColor(Color4.rgba(gr, gr, gr, 0.3f),
                Color4.rgba(gr, gr, gr, 0.3f),
                Color4.rgba(1, 1, 1, 0.5f),
                Color4.rgba(1, 1, 1, 0.5f));
        backlight.setAlpha(0);
        icon = new TextureSprite(c);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    private void onPressAnim() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<ColorRectSprite>(backlight, "alpha")
                .transform(backlight.getAlpha(), 0, Easing.None)
                .transform(1, 40, Easing.None));
        ComplexAnimation anim = builder.build();
        anim.setOnProgressListener(invalideDrawDuringAnimation());
        anim.start();
        setAnimation(anim);
    }

    private void offPressAnim() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<ColorRectSprite>(backlight, "alpha")
                .transform(backlight.getAlpha(), 0, Easing.None)
                .transform(0, 150, Easing.None));
        ComplexAnimation anim = builder.build();
        anim.setOnProgressListener(invalideDrawDuringAnimation());
        anim.start();
        setAnimation(anim);
    }

    @Override
    public void onClickEvent() {

        super.onClickEvent();
        if (onClickListener != null) onClickListener.onClick(this);
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

    public void setIcon(AbstractTexture t) {
        icon.setTexture(t);
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {

        super.onDraw(canvas);

        backlight.setArea(RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()));
        backlight.draw(canvas);

        icon.setPosition(canvas.getWidth() / 2f, canvas.getHeight() / 2f);
        icon.setAreaFitTexture(RectF.anchorOWH(Anchor.Center, 0, 0, canvas.getWidth() * 0.5f, canvas.getHeight() * 0.5f));
        icon.draw(canvas);
    }

}
