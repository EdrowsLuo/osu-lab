package com.edplan.osulab.ui.scenes.songs;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.pieces.TextButton;
import com.edplan.osulab.ui.scenes.Scenes;

public class DetailsPanel extends RelativeLayout implements Hideable {
    public static float WIDTH_DP = 350;

    public DetailsPanel(MContext c) {
        super(c);
        setBackground(Color4.rgba(0, 0, 0, 0.2f));


        addAll(
                new TextButton(c) {{
                    layoutParam(
                            new RelativeParam() {{
                                width = Param.makeUpDP(200);
                                height = Param.makeUpDP(60);
                                gravity = Gravity.Center;
                            }}
                    );
                    setText("testGame");
                    setOnClickListener(view -> LabGame.get().getScenes().swapScene(ScenesName.GameScene));
                }}
        );

    }

    @Override
    public void hide() {
        ComplexAnimationBuilder b = ComplexAnimationBuilder.
                //start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
                        start(new FloatQueryAnimation<EdView>(this, "offsetX")
                        .transform(0, 0, Easing.None)
                        .transform(-ViewConfiguration.dp(WIDTH_DP), ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.InQuad));
        ComplexAnimation anim = b.buildAndStart();
        setAnimation(anim);
    }

    @Override
    public void show() {
        setAlpha(0);
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        b.together(new FloatQueryAnimation<EdView>(this, "offsetX")
                .transform(-ViewConfiguration.dp(WIDTH_DP), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME * 2, Easing.OutQuad));
        ComplexAnimation anim = b.buildAndStart();
        setAnimation(anim);
    }


    @Override
    public boolean isHidden() {
        return getVisiblility() == VISIBILITY_GONE;
    }

}
