package com.edplan.osulab.ui.toolbar;

import com.edplan.framework.Framework;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ui.pieces.SongPanel;

public class Toolbar extends RelativeLayout implements Hideable {
    private float normalBaseAlpha = 0.7f;

    private float highlightBaseAlpha = 1;

    private float baseAlpha = normalBaseAlpha;

    private float settedAlpha = 1;

    private float normalShadowHeight = 7;

    private float highlightShadowHeight = 40;

    private float shadowHeight = 7;

    private boolean highlight = false;

    private LinearLayout leftLayout;

    private LinearLayout rightLayout;

    private ColorRectSprite shadowSprite;

    private double preTouchTime;

    public ToolbarShadow shadow;

    Color4 dividerColor = Color4.rgba(1, 1, 1, 0.4f);

    public Toolbar(MContext c) {
        super(c);
        shadow = new ToolbarShadow(c);
        setClickable(true);
        setBackgroundColor(Color4.rgba(0, 0, 0, 0.5f));
        shadowSprite = new ColorRectSprite(c);
        float gr = 0f;

        shadowSprite.setColor(Color4.rgba(gr, gr, gr, 0.6f),
                Color4.rgba(gr, gr, gr, 0.6f),
                Color4.rgba(0, 0, 0, 0f),
                Color4.rgba(0, 0, 0, 0f));



        addAll(
                leftLayout = new LinearLayout(c){{
                    setOrientation(Orientation.DIRECTION_L2R);

                    layoutParam(
                            new RelativeParam(){{
                                gravity = Gravity.CenterLeft;
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_MATCH_PARENT;
                            }}
                    );

                    children(
                            iconButton(
                                    FontAwesome.fa_navicon.getTexture(),
                                    view -> LabGame.get().getOptionList().changeState()
                            ),
                            divider()
                    );
                }},
                rightLayout = new LinearLayout(c){{
                    setOrientation(Orientation.DIRECTION_L2R);

                    layoutParam(
                            new RelativeParam(){{
                                gravity = Gravity.CenterRight;
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_MATCH_PARENT;
                            }}
                    );

                    children(
                            iconButton(
                                    FontAwesome.fa_music.getTexture(),
                                    view -> SongPanel.getInstance(getContext()).changeState()
                            ),
                            divider(),
                            iconButton(
                                    FontAwesome.fa_angle_double_down.getTexture(),
                                    view -> LabGame.get().getSceneOverlay().changeState()
                            ),
                            divider(),
                            iconButton(
                                    FontAwesome.fa_genderless.getTexture(),
                                    view ->  LabGame.get().getMessageList().changeState()
                            )
                    );

                }}
        );
        setAlpha(1);
    }

    private EdView iconButton(AbstractTexture icon, OnClickListener onClickListener) {
        return new ToolBarButton(getContext()) {{
            setIcon(icon);
            setGravity(Gravity.Center);
            layoutParam(
                    new MarginLayoutParam() {{
                        width = Param.makeupScaleOfParentOtherParam(1.6f);
                        height = Param.MODE_MATCH_PARENT;
                    }}
            );
            setOnClickListener(onClickListener);
        }};
    }

    private EdView divider() {
        return new EdView(getContext()){{
            setBackgroundColor(dividerColor);
            layoutParam(
                    new MarginLayoutParam(){{
                        width = Param.makeUpDP(2f);
                        height = Param.MODE_MATCH_PARENT;
                        marginBottom = ViewConfiguration.dp(4);
                        marginTop = ViewConfiguration.dp(4);
                    }}
            );
        }};
    }

    @Override
    public void onInitialLayouted() {

        super.onInitialLayouted();
        directHide();
    }

    @Override
    public boolean onMotionEvent(EdMotionEvent e) {

        preTouchTime = Framework.relativePreciseTimeMillion();
        if (!highlight) {
            highlightOn();
        }
        return super.onMotionEvent(e);
    }

    public void setShadowHeight(float shadowHeight) {
        this.shadowHeight = shadowHeight;
    }

    public float getShadowHeight() {
        return shadowHeight;
    }

    @Override
    public void hide() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        builder.together(new FloatQueryAnimation<Toolbar>(this, "offsetY")
                .transform(getOffsetY(), 0, Easing.None)
                .transform(-getHeight(), ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.InQuad));
        ComplexAnimation anim = builder.build();
        anim.setOnFinishListener(() -> setVisiblility(VISIBILITY_GONE));
        anim.start();
        setAnimation(anim);
    }

    public void directHide() {
        setVisiblility(VISIBILITY_GONE);
        setOffsetY(-getHeight());
        setAlpha(0);
    }

    @Override
    public void show() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this, "alpha")
                .transform(getAlpha(), 0, Easing.None)
                .transform(1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        builder.together(new FloatQueryAnimation<Toolbar>(this, "offsetY")
                .transform(getOffsetY(), 0, Easing.None)
                .transform(0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutQuad));
        ComplexAnimation anim = builder.build();
        anim.start();
        setAnimation(anim);
        setVisiblility(VISIBILITY_SHOW);
    }

    private void postHighlightOff(double offset) {
        post(() -> {
            if (Framework.relativePreciseTimeMillion() - preTouchTime > 700) {
                highlightOff();
            } else {
                postHighlightOff(100);
            }
        }, offset);
    }

    public void highlightOn() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this, "baseAlpha")
                .transform(getBaseAlpha(), 0, Easing.None)
                .transform(highlightBaseAlpha, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        builder.together(new FloatQueryAnimation<Toolbar>(this, "shadowHeight")
                .transform(getShadowHeight(), 0, Easing.None)
                .transform(highlightShadowHeight, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.OutQuad));
        ComplexAnimation anim = builder.build();
        anim.setOnFinishListener(() -> postHighlightOff(1000));
        anim.start();
        setAnimation(anim);
        highlight = true;
    }

    public void highlightOff() {
        ComplexAnimationBuilder builder = ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this, "baseAlpha")
                .transform(getBaseAlpha(), 0, Easing.None)
                .transform(normalBaseAlpha, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        builder.together(new FloatQueryAnimation<Toolbar>(this, "shadowHeight")
                .transform(getShadowHeight(), 0, Easing.None)
                .transform(normalShadowHeight, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.InQuad));
        ComplexAnimation anim = builder.build();
        anim.setOnFinishListener(() -> highlight = false);
        anim.start();
        setAnimation(anim);
    }


    public void setBaseAlpha(float baseAlpha) {
        this.baseAlpha = baseAlpha;
        updateAlpha();
    }

    public float getBaseAlpha() {
        return baseAlpha;
    }

    @Override
    public void setAlpha(float alpha) {

        settedAlpha = alpha;
        shadowSprite.setAlpha(alpha);
        updateAlpha();
    }

    @Override
    public float getAlpha() {

        return settedAlpha;
    }

    @Override
    public boolean isHidden() {

        return getVisiblility() == VISIBILITY_GONE;
    }

    protected void updateAlpha() {
        super.setAlpha(settedAlpha * baseAlpha);
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
    }

    public class ToolbarShadow extends EdView {

        public ToolbarShadow(MContext c) {
            super(c);
        }

        @Override
        public float getOffsetX() {
            return Toolbar.this.getOffsetX();
        }

        @Override
        public float getOffsetY() {
            return Toolbar.this.getOffsetY();
        }

        @Override
        protected void onDraw(BaseCanvas canvas) {
            super.onDraw(canvas);
            shadowSprite.setArea(RectF.xywh(0, canvas.getHeight(), canvas.getWidth(), ViewConfiguration.dp(shadowHeight)));
            shadowSprite.draw(canvas);
        }
    }
}
