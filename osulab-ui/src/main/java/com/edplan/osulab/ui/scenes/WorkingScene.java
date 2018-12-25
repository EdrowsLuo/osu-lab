package com.edplan.osulab.ui.scenes;

import com.edplan.framework.MContext;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.osulab.LabGame;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.Anchor;

public class WorkingScene extends BaseScene {

    private String name;

    private TextView nameView;

    public WorkingScene(MContext c) {
        super(c);
        setBackgroundColor(Color4.gray(0.2f).setAlpha(0.5f));

        addAll(
                new TextView(c) {{
                    setFont(BMFont.Exo_20_Semi_Bold);
                    setTextSize(ViewConfiguration.dp(40));

                    layoutParam(
                            new RelativeParam() {{
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_WRAP_CONTENT;
                                gravity = Gravity.Center;
                            }}
                    );
                }}
        );
    }

    public void setSceneName(String s) {
        name = s;
        nameView.setText("[" + s + "]\nWORKING!");
    }

    @Override
    public void hide() {
        ComplexAnimationBuilder bd = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 0, Scenes.SCENE_TRANSITION_DURATION, Easing.None));
        ComplexAnimation anim = bd.buildAndStart();
        anim.setOnFinishListener(setVisibilityWhenFinish(VISIBILITY_GONE));
        setAnimation(anim);
    }

    @Override
    public void show() {

        setVisiblility(VISIBILITY_SHOW);
        ComplexAnimationBuilder bd = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 1, Scenes.SCENE_TRANSITION_DURATION, Easing.None));
        ComplexAnimation anim = bd.buildAndStart();
        setAnimation(anim);

        BaseBoundOverlay bo = new BaseBoundOverlay();
        RectF r = RectF.anchorOWH(Anchor.Center, getWidth(), getHeight(), ViewConfiguration.dp(150), ViewConfiguration.dp(150));
        bo.setLeft(r.getLeft());
        bo.setTop(r.getTop());
        bo.setRight(r.getRight());
        bo.setBottom(r.getBottom());
        LabGame.get().getJumpingCircle().setBoundOverlay(bo);
    }

    @Override
    public String getSceneName() {

        return name;
    }

}
