package com.edplan.osulab.ui.popup;

import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;

public class PopupToast extends PopupView {
    private float hideOffsetX = ViewConfiguration.dp(100);

    TextView text;

    public PopupToast(MContext c) {
        super(c);
        setBackgroundRoundedRect(Color4.rgba(0, 0, 0, 0.4f),ViewConfiguration.dp(5));

        setLayoutParam(
                new RelativeLayout.RelativeParam() {{
                    width = Param.MODE_WRAP_CONTENT;
                    height = Param.MODE_WRAP_CONTENT;
                    gravity = Gravity.BottomCenter;
                    marginBottom = ViewConfiguration.dp(60);
                }});

        children(
                new LinearLayout(c){{
                    gravity(Gravity.Center);

                    layoutParam(
                            new RelativeLayout.RelativeParam() {{
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_WRAP_CONTENT;
                                gravity = Gravity.BottomCenter;
                            }});

                    children(
                            text = new TextView(c){{
                                setTextSize(ViewConfiguration.dp(17));

                                layoutParam(
                                        new MarginLayoutParam() {{
                                            width = Param.MODE_WRAP_CONTENT;
                                            height = Param.MODE_WRAP_CONTENT;
                                            postMargin(ViewConfiguration.dp(6));
                                            marginLeft = ViewConfiguration.dp(10);
                                            marginRight = ViewConfiguration.dp(10);
                                        }});
                            }}
                    );

                }}
        );
    }

    @Override
    protected void onHide() {

        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "offsetY")
                .transform(getOffsetY(), 0, Easing.None)
                .transform(hideOffsetX, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutQuad));
        ComplexAnimation anim = b.buildAndStart();
        anim.setOnFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {

                PopupToast.super.onHide();
            }
        });
        setAnimation(anim);
    }

    @Override
    protected void onShow() {

        super.onShow();
        setAlpha(0);
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "offsetY")
                .transform(hideOffsetX, 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutQuad));
        ComplexAnimation anim = b.buildAndStart();
        setAnimation(anim);

        post(new Runnable() {
            @Override
            public void run() {

                hide();
            }
        }, 1500);

    }

    public void setText(String t) {
        text.setText(t);
    }

    public static PopupToast toast(MContext c, String text) {
        PopupToast t = new PopupToast(c);
        t.setText(text);
        return t;
    }

}
