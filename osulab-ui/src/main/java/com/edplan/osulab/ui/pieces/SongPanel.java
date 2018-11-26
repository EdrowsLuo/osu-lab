package com.edplan.osulab.ui.pieces;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.TextureView;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.osulab.ui.popup.PopupToast;
import com.edplan.framework.ui.inputs.EdMotionEvent;

public class SongPanel extends PopupView {
    private static SongPanel instance;

    private float hideOffset = ViewConfiguration.dp(100);

    public SongPanel(MContext c) {
        super(c);
        setLayoutParam(
                new RelativeLayout.RelativeParam() {{
                    width = Param.makeUpDP(300);
                    height = Param.makeUpDP(150);
                    gravity = Gravity.TopRight;
                    marginTop = ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP + 10);
                    marginRight = ViewConfiguration.dp(10);
                }});
        setBackgroundRoundedRect(Color4.Black,ViewConfiguration.dp(20));
        setClickable(true);
        setOutsideTouchable(true);
    }

    @Override
    public boolean onOutsideTouch(EdMotionEvent e) {
        hide();
        return true;
    }

    @Override
    protected void onHide() {
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "offsetX")
                .transform(0, 0, Easing.None)
                .transform(hideOffset, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.InQuad));
        ComplexAnimation anim = b.buildAndStart();
        anim.setOnFinishListener(() -> SongPanel.super.onHide());
        setAnimation(anim);
    }

    @Override
    protected void onShow() {
        super.onShow();
        setAlpha(0);
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "offsetX")
                .transform(hideOffset, 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME * 2, Easing.OutBounce));
        ComplexAnimation anim = b.buildAndStart();
        setAnimation(anim);
    }

    public static SongPanel getInstance(MContext context) {
        if (instance == null) {
            instance = new SongPanel(context);
        }
        return instance;
    }
}
