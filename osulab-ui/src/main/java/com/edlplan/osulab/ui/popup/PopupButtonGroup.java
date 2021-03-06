package com.edlplan.osulab.ui.popup;

import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.additions.popupview.PopupView;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.animation.callback.OnFinishListener;
import com.edlplan.framework.ui.inputs.EdMotionEvent;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.Orientation;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.osulab.ui.pieces.LabButton;
import com.edlplan.osulab.ui.pieces.TextButton;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.graphics.opengl.objs.Color4;

public class PopupButtonGroup extends PopupView {

    private boolean hideOnOutsidePress = true;

    private LinearLayout buttons;

    public PopupButtonGroup(MContext c) {
        super(c);
        setOutsideTouchable(true);
        setClickable(true);
        setBackgroundColor(Color4.rgba(0, 0, 0, 0.7f));

        setLayoutParam(
                new RelativeLayout.RelativeParam() {{
                    width = Param.MODE_MATCH_PARENT;
                    height = Param.makeUpDP(LabButton.DEFAULT_HEIGHT + 10);
                    gravity = Gravity.Center;
                }});

        children(
                buttons = new LinearLayout(c) {{
                    setOrientation(Orientation.DIRECTION_L2R);
                    setChildoffset(ViewConfiguration.dp(5));

                    layoutParam(
                            new RelativeLayout.RelativeParam() {{
                                width = Param.MODE_MATCH_PARENT;
                                height = Param.MODE_MATCH_PARENT;
                                gravity = Gravity.Center;
                            }});
                }}
        );
    }

    public void addButton(String text, OnClickListener l) {
        TextButton button = new TextButton(getContext());
        button.setText(text);
        button.setOnClickListener(l);
        MarginLayoutParam p = new MarginLayoutParam();
        p.width = Param.MODE_WRAP_CONTENT;
        p.height = Param.makeUpDP(LabButton.DEFAULT_HEIGHT);
        buttons.addView(button, p);
    }

    public void setHideOnOutsidePress(boolean hideOnOutsidePress) {
        this.hideOnOutsidePress = hideOnOutsidePress;
    }

    public boolean isHideOnOutsidePress() {
        return hideOnOutsidePress;
    }

    @Override
    public boolean onOutsideTouch(EdMotionEvent e) {

        if (hideOnOutsidePress) {
            hide();
        }
        return true;
    }

    @Override
    protected void onHide() {

        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutQuad));
        ComplexAnimation anim = b.buildAndStart();
        anim.setOnFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {

                PopupButtonGroup.super.onHide();
            }
        });
        setAnimation(anim);
    }

    @Override
    protected void onShow() {

        super.onShow();
        setAlpha(0);
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.InQuad));
        ComplexAnimation anim = b.buildAndStart();
        setAnimation(anim);
    }
}
