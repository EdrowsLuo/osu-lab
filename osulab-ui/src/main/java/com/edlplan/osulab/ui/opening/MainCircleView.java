package com.edlplan.osulab.ui.opening;

import com.edlplan.osulab.LabGame;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.ui.drawable.sprite.CircleSprite;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.graphics.opengl.objs.Color4;

import com.edlplan.framework.ui.animation.callback.OnFinishListener;

public class MainCircleView extends EdView {
    private float PieceSize = 0.4f;


    private CircleSprite p1, p2, p3, p4;


    public MainCircleView(MContext c) {
        super(c);

        p1 = new CircleSprite(c);
        p2 = new CircleSprite(c);
        p3 = new CircleSprite(c);
        p4 = new CircleSprite(c);

    }

    public float getBaseSize() {
        return getWidth() / 2;
    }

    public void setPieceOriginOffset(float length) {
        Vec2 pos = new Vec2(getBaseSize(), getHeight() / 2);
        Vec2 org = pos.copy();
        pos.move(length, 0);
        final float progress = length / (getBaseSize());
        pos.rotate(org, FMath.Pi * progress);
        p1.setPosition(pos.x, pos.y);
        pos.rotate(org, FMath.PiHalf);
        p2.setPosition(pos.x, pos.y);
        pos.rotate(org, FMath.PiHalf);
        p3.setPosition(pos.x, pos.y);
        pos.rotate(org, FMath.PiHalf);
        p4.setPosition(pos.x, pos.y);
        float alpha = FMath.linearCut(
                progress,
                0, 0.95f, 1,
                0, 1, 0);
        p1.setAlpha(alpha);
        p2.setAlpha(alpha);
        p3.setAlpha(alpha);
        p4.setAlpha(alpha);
        final float radius = getBaseSize() * PieceSize * FMath.linearCut(
                progress,
                0, 0.7f, 1,
                0, 1, 0);
        p1.setRadius(radius);
        p2.setRadius(radius);
        p3.setRadius(radius);
        p4.setRadius(radius);
    }

    public void startOpeningAnim(final OnFinishListener l) {

        p1.resetRadius();
        p2.resetRadius();
        p3.resetRadius();
        p4.resetRadius();


        float unit = getBaseSize() * PieceSize;
        p1.setArea(RectF.ltrb(-unit, -unit, unit, unit));
        p1.setAccentColor(Color4.rgb255(255, 125, 183));
        p2.setArea(RectF.ltrb(-unit, -unit, unit, unit));
        p2.setAccentColor(Color4.rgb255(0, 192, 254));
        p3.setArea(RectF.ltrb(-unit, -unit, unit, unit));
        p3.setAccentColor(Color4.rgb255(252, 243, 106));
        p4.setArea(RectF.ltrb(-unit, -unit, unit, unit));
        p4.setAccentColor(Color4.rgb255(255, 255, 255));

        float radius = getBaseSize();

        LabGame.get().getJumpingCircle().startOpeningAnimation(() -> post(LabGame.get().getToolBar()::show, 500));

        setAnimation(new ComplexAnimationBuilder(){{
            startAnim(
                    new FloatQueryAnimation<MainCircleView>(MainCircleView.this::setPieceOriginOffset) {{
                        transform(0, 0, Easing.None);
                        transform(radius, 600, Easing.OutExpo);
                    }}
            );

            onBuild(v -> {
                v.setOnFinishListener(()->{
                    if (l != null) l.onFinish();
                    setVisiblility(VISIBILITY_GONE);
                });
                v.start();
            });
        }}.build());
    }

    @Override
    public void onInitialLayouted() {
        super.onInitialLayouted();

        post(new Runnable() {
            @Override
            public void run() {

                startOpeningAnim(null);
                //getContext().toast("开始动画");
            }
        }, 1000);
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {

        super.onDraw(canvas);
        p1.draw(canvas);
        p2.draw(canvas);
        p3.draw(canvas);
        p4.draw(canvas);
    }
}
