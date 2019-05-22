package com.edlplan.osulab.ui.scenes;

import com.edlplan.osulab.LabGame;
import com.edlplan.osulab.ScenesName;
import com.edlplan.osulab.ui.BackQuery;
import com.edlplan.osulab.ui.UiConfig;
import com.edlplan.osulab.ui.popup.PopupToast;
import com.edlplan.framework.Framework;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.Anchor;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.animation.callback.OnFinishListener;
import com.edlplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edlplan.framework.ui.inputs.EdMotionEvent;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.ui.layout.Orientation;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.component.Hideable;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.ui.text.font.FontAwesome;

import java.util.HashMap;

import com.edlplan.osulab.ui.pieces.JumpingCircle;

public class SceneSelectButtonBar extends RelativeLayout implements Hideable, BackQuery.BackHandler {
    public static final String GROUP_MAIN = "main";

    public static final String GROUP_PLAY = "play";

    private HashMap<String, ButtonGroup> groups = new HashMap<String, ButtonGroup>();

    private float buttonSize = 100;

    private float dividePoint = 0.3f;

    private float circleSize = 0.25f;

    private float shadowDp = 9;

    private LinearLayout leftLayout;

    private LinearLayout rightLayout;

    private ButtonGroup currentGroup;

    private ColorRectSprite shadowSpriteTop, shadowSpriteBottom;

    private String currentGroupName = GROUP_MAIN;

    private OnClickListener soloClicker;

    public SceneSelectButtonBar(MContext c) {
        super(c);
        currentGroup = new ButtonGroup();
        shadowSpriteTop = new ColorRectSprite(c);
        shadowSpriteTop.setColor(Color4.rgba(0, 0, 0, 0),
                Color4.rgba(0, 0, 0, 0),
                Color4.rgba(0, 0, 0, 0.2f),
                Color4.rgba(0, 0, 0, 0.2f));
        shadowSpriteBottom = new ColorRectSprite(c);
        shadowSpriteBottom.setColor(Color4.rgba(0, 0, 0, 0.5f),
                Color4.rgba(0, 0, 0, 0.5f),
                Color4.rgba(0, 0, 0, 0),
                Color4.rgba(0, 0, 0, 0));

        setBackgroundColor(UiConfig.Color.BLUE_DEEP_DARK.copyNew().setAlpha(0.9f));


        addAll(
                leftLayout = new LinearLayout(c){{
                    setBackwardsDraw(true);
                    setOrientation(Orientation.DIRECTION_R2L);

                    layoutParam(
                            new RelativeParam(){{
                                width = Param.makeupScaleOfParentParam(dividePoint);
                                height = Param.MODE_MATCH_PARENT;
                                gravity = Gravity.CenterLeft;
                            }}
                    );
                }},
                rightLayout = new LinearLayout(c){{
                    setBackwardsDraw(true);
                    setOrientation(Orientation.DIRECTION_R2L);
                    setGravity(Gravity.CenterLeft);

                    layoutParam(
                            new RelativeParam(){{
                                width = Param.makeupScaleOfParentParam(1 - dividePoint - 0.1f);
                                height = Param.MODE_MATCH_PARENT;
                                gravity = Gravity.CenterRight;
                            }}
                    );
                }}
        );

        loadGroup(
                GROUP_MAIN,
                new EdView[]{
                        createButton(
                                Color4.gray(0.2f),
                                "Setting",
                                FontAwesome.fa_cogs.getTexture(),
                                view -> LabGame.get().getOptionList().show()
                        ).setLeftShadow()
                },
                new EdView[]{
                        createButton(
                                UiConfig.Color.BLUE,
                                "Play",
                                FontAwesome.fa_osu_logo.getTexture(),
                                view -> swapGroup(GROUP_PLAY)
                        ),
                        createButton(
                                UiConfig.Color.YELLOW,
                                "Download",
                                FontAwesome.fa_osu_chevron_down_o.getTexture(),
                                view -> PopupToast.toast(getContext(), "working").show()
                        ),
                        createButton(
                                UiConfig.Color.PINK,
                                "Exit",
                                FontAwesome.fa_osu_cross_o.getTexture(),
                                view -> {
                                    hide();
                                    LabGame.get().exit();
                                }
                        )
                });

        loadGroup(
                GROUP_PLAY,
                new EdView[]{
                        createButton(
                                Color4.gray(0.3f),
                                "Back",
                                FontAwesome.fa_backward.getTexture(),
                                view -> swapGroup(GROUP_MAIN)
                        ).setLeftShadow()
                },
                new EdView[]{
                        createButton(
                                UiConfig.Color.BLUE_DEEP_DARK,
                                "Solo",
                                FontAwesome.fa_osu_logo.getTexture(),
                                soloClicker = view -> {
                                    hide();
                                    LabGame.get().getScenes().swapScene(ScenesName.SongSelect);
                                }
                        ),
                        createButton(
                                UiConfig.Color.BLUE_DARK,
                                "Multiple",
                                FontAwesome.fa_osu_multi.getTexture(),
                                view -> PopupToast.toast(getContext(), "working").show()
                        ),
                        createButton(
                                UiConfig.Color.BLUE_LIGHT,
                                "Edit",
                                FontAwesome.fa_osu_edit_o.getTexture(),
                                view -> PopupToast.toast(getContext(), "working").show()
                        )
                }
        );

        applyGroup(getGroup(GROUP_MAIN));
    }


    private void loadGroup(String name, EdView[] left, EdView[] right) {
        currentGroup = new ButtonGroup();
        leftLayout.setChildrenWrapper(new ChildrenWrapper());
        rightLayout.setChildrenWrapper(new ChildrenWrapper());

        currentGroup.leftWrapper = leftLayout.getChildrenWrapper();
        currentGroup.rightWrapper = rightLayout.getChildrenWrapper();
        groups.put(name, currentGroup);

        leftLayout.children(left);
        rightLayout.children(right);
    }



    private SceneSelectButton createButton(Color4 bgColor, String text, AbstractTexture texture, OnClickListener listener) {
        return new SceneSelectButton(getContext()) {{
            setBackgroundColor(bgColor);
            setText(text);
            setOnClickListener(listener);
            setTexture(texture);

            layoutParam(
                    new MarginLayoutParam() {{
                        height = Param.MODE_MATCH_PARENT;
                        width = Param.makeUpDP(buttonSize);
                    }}
            );
        }};
    }


    public OnClickListener getSoloClicker() {
        return soloClicker;
    }

    public String getCurrentGroupName() {
        return currentGroupName;
    }

    private ButtonGroup getGroup(String name) {
        return groups.get(name);
    }

    protected void applyGroup(ButtonGroup g) {
        leftLayout.setChildrenWrapper(g.leftWrapper);
        rightLayout.setChildrenWrapper(g.rightWrapper);
        currentGroup = g;
        invalidateDraw();
    }

    protected void directSwap(String name) {
        currentGroupName = name;
        applyGroup(getGroup(name));
    }

    public void swapGroup(final String name) {
        final ButtonGroup g = getGroup(name);
        if (g == null || currentGroup == g) return;
        for (EdView v : currentGroup.leftWrapper) {
            ((SceneSelectButton) v).hide();
        }

        for (EdView v : currentGroup.rightWrapper) {
            ((SceneSelectButton) v).hide();
        }

        post(new Runnable() {
            @Override
            public void run() {

                directSwap(name);
                currentGroup.show();
                //PopupToast.toast(getContext(),"swap to "+name).show();
            }
        }, SceneSelectButton.ANIM_DURATION);
    }

    @Override
    public boolean onBack() {

        switch (currentGroupName) {
            case GROUP_MAIN:
                return false;
            case GROUP_PLAY:
                swapGroup(GROUP_MAIN);
                return true;
        }
        return false;
    }


    @Override
    public void onInitialLayouted() {

        super.onInitialLayouted();
        directHide();
    }

    protected void directHide() {
        setVisiblility(VISIBILITY_GONE);
        setAlpha(0);
        invalidateDraw();
    }


    @Override
    public void hide() {

        BackQuery.get().unregist(this);
        LabGame.get().getJumpingCircle().setBoundOverlay(null);

        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButtonBar>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        ComplexAnimation anim = builder.build();
        anim.setOnFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {

                setVisiblility(VISIBILITY_GONE);
            }
        });
        anim.start();
        setAnimation(anim);

        currentGroup.hide();

    }

    @Override
    public void show() {

        BackQuery.get().registNoBackButton(this);
        directSwap(GROUP_MAIN);
        BaseBoundOverlay bound = new BaseBoundOverlay();
        RectF area = RectF.anchorOWH(Anchor.Center,
                getLeft() + getWidth() * dividePoint,
                (getTop() + getBottom()) / 2,
                getWidth() * circleSize,
                getWidth() * circleSize);
        bound.setLeft(area.getLeft());
        bound.setTop(area.getTop());
        bound.setRight(area.getRight());
        bound.setBottom(area.getBottom());
        LabGame.get().getJumpingCircle().setBoundOverlay(bound);


        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButtonBar>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        ComplexAnimation anim = builder.build();
        anim.setOnFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {

                update();
                delayHide(5000);
            }
        });
        anim.start();
        setAnimation(anim);

        currentGroup.show();

        setVisiblility(VISIBILITY_SHOW);
    }

    @Override
    public boolean isHidden() {

        return getVisiblility() == VISIBILITY_GONE;
    }

    private void delayHide(double delay) {
        post(() -> {
            if (Framework.relativePreciseTimeMillion() - updateClock > 10000) {
                if (!isHidden()) hide();
            } else {
                delayHide(2000);
            }
        }, delay);
    }

    public void update() {
        updateClock = Framework.relativePreciseTimeMillion();
    }

    double updateClock;

    @Override
    public boolean onMotionEvent(EdMotionEvent e) {

        update();
        return super.onMotionEvent(e);
    }

    @Override
    protected void onDrawBackgroundLayer(BaseCanvas canvas) {
        super.onDrawBackgroundLayer(canvas);
        float shadowScale = 0.5f + JumpingCircle.glowProgress / 2;

        shadowSpriteTop.setArea(RectF.anchorOWH(Anchor.BottomLeft, 0, 0, canvas.getWidth(), ViewConfiguration.dp(shadowDp * shadowScale) * 0.5f));
        shadowSpriteTop.draw(canvas);

        shadowSpriteBottom.setArea(RectF.anchorOWH(Anchor.TopLeft, 0, canvas.getHeight(), canvas.getWidth(), ViewConfiguration.dp(shadowDp * shadowScale)));
        shadowSpriteBottom.draw(canvas);
    }

    private class ButtonGroup implements Hideable {
        public ChildrenWrapper leftWrapper, rightWrapper;
        public Runnable onHide;
        public Runnable onShow;

        @Override
        public void hide() {

            if (onHide != null) onHide.run();
            for (EdView v : leftWrapper) {
                ((SceneSelectButton) v).hide();
            }

            for (EdView v : rightWrapper) {
                ((SceneSelectButton) v).hide();
            }
        }

        @Override
        public void show() {

            if (onShow != null) onShow.run();
            for (EdView v : leftWrapper) {
                ((SceneSelectButton) v).show();
            }

            for (EdView v : rightWrapper) {
                ((SceneSelectButton) v).show();
            }
        }

        @Override
        public boolean isHidden() {

            return currentGroup != this;
        }
    }
}
