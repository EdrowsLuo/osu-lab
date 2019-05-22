package com.edlplan.osulab.ui.scenes.songs;

import com.edlplan.osulab.LabGame;
import com.edlplan.osulab.ScenesName;
import com.edlplan.osulab.ui.scenes.game.GameScene;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.Orientation;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.EditText;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.component.Hideable;
import com.edlplan.osulab.ui.pieces.TextButton;

public class DetailsPanel extends RelativeLayout implements Hideable {
    public static float WIDTH_DP = 350;
    EditText dir;

    public DetailsPanel(MContext c) {
        super(c);
        setBackgroundColor(Color4.rgba(0, 0, 0, 0.2f));


        addAll(
                new LinearLayout(c) {{
                    orientation = Orientation.DIRECTION_T2B;
                    layoutParam(
                            new RelativeParam() {{
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_WRAP_CONTENT;
                                gravity = Gravity.Center;
                            }}
                    );
                    children(
                            dir = new EditText(c) {{
                                setBackgroundColor(Color4.rgba(0, 0, 0, 0.5f));
                                layoutParam(
                                        new RelativeParam() {{
                                            width = Param.MODE_WRAP_CONTENT;
                                            height = Param.MODE_WRAP_CONTENT;
                                            gravity = Gravity.Center;
                                        }}
                                );
                                getTextView().setTextSize(ViewConfiguration.dp(20));
                            }},
                            new TextButton(c) {{
                                layoutParam(
                                        new RelativeParam() {{
                                            width = Param.makeUpDP(200);
                                            height = Param.makeUpDP(60);
                                            gravity = Gravity.Center;
                                        }}
                                );
                                setText("testGame");
                                setOnClickListener(view -> {
                                    GameScene.testOsu = dir.getTextView().getText();
                                    LabGame.get().getScenes().swapScene(ScenesName.GameScene);
                                });
                            }}
                    );
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
