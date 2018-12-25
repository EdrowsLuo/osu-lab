package com.edplan.osulab.ui;

import android.app.Dialog;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.drawable.ColorDrawable;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.widget.ScrollLayout;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.osulab.ui.pieces.TextButton;

/**
 *
 */
public class SceneOverlay extends RelativeLayout implements Hideable {
    TextButton button;
    TextView text;


    public SceneOverlay(MContext c) {
        super(c);
        //setClickable(true);
        ColorDrawable cd = new ColorDrawable(c);
        cd.setColor(Color4.rgba(0, 0, 0, 0.9f),
                Color4.rgba(0, 0, 0, 0.9f),
                Color4.rgba(0, 0, 0, 0.8f),
                Color4.rgba(0, 0, 0, 0.8f));
        setBackgroundColor(cd);
        {
            button = new TextButton(c);
            button.setText("BUTTON");
            RelativeLayout.RelativeParam p1 = new RelativeLayout.RelativeParam();
            p1.marginLeft = p1.marginRight = ViewConfiguration.dp(10);
            p1.width = Param.MODE_MATCH_PARENT;
            p1.height = Param.makeUpDP(30);
            //p1.marginTop=ViewConfiguration.dp(30);
            button.setOnClickListener(view -> {
                updateText();
                getContext().getHoldingView().post(()->{
                    Dialog dialog = new Dialog(getContext().getNativeContext());
                    android.widget.TextView textView = new android.widget.TextView(getContext().getNativeContext());
                    textView.setText("hello!");
                    dialog.setContentView(textView);
                    dialog.show();
                });
            });
            addView(button, p1);
        }
        {
            ScrollLayout sc = new ScrollLayout(c);
            sc.setOrientation(Orientation.DIRECTION_T2B);
            RelativeLayout.RelativeParam p1 = new RelativeLayout.RelativeParam();
            p1.marginTop = ViewConfiguration.dp(30);
            p1.width = Param.MODE_MATCH_PARENT;
            p1.height = Param.MODE_MATCH_PARENT;
            //p1.marginTop=ViewConfiguration.dp(30);
            addView(sc, p1);
            {
                text = new TextView(c);
                text.setText("[SceneOverlay]");
                text.setGravity(Gravity.TopLeft);
                text.setTextSize(ViewConfiguration.dp(25));
                RelativeLayout.RelativeParam p = new RelativeLayout.RelativeParam();
                p.width = Param.MODE_MATCH_PARENT;
                p.height = Param.MODE_WRAP_CONTENT;
                //p.marginTop=ViewConfiguration.dp(30);
                p.gravity = Gravity.TopCenter;
                sc.addView(text, p);
            }
        }

    }

    protected void updateText() {
        String t = getContext().getViewRoot().loadViewTreeStruct();
        text.setText(t);
        System.out.println(t);
    }

    @Override
    public void onInitialLayouted() {
        super.onInitialLayouted();
        directHide();
    }

    @Override
    public void hide() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<>(this::setAlpha)
                .transform(getAlpha(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        builder.together(new FloatQueryAnimation<>(this::setOffsetY)
                .transform(getOffsetY(), 0, Easing.None)
                .transform(getHeight(), ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.InQuad));
        ComplexAnimation anim = builder.build();
        anim.setOnFinishListener(() -> {
            setVisiblility(VISIBILITY_GONE);
            BackQuery.get().unregist(SceneOverlay.this);
        });
        anim.start();
        setAnimation(anim);
    }

    public void directHide() {
        setVisiblility(VISIBILITY_GONE);
        setOffsetY(getHeight());
        setAlpha(0);
    }

    @Override
    public void show() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneOverlay>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        builder.together(new FloatQueryAnimation<SceneOverlay>(this, "offsetY")
                .transform(getOffsetY(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutQuad));
        ComplexAnimation anim = builder.build();
        anim.start();
        setAnimation(anim);
        setVisiblility(VISIBILITY_SHOW);
        BackQuery.get().regist(this);
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() == VISIBILITY_GONE;
    }
}
