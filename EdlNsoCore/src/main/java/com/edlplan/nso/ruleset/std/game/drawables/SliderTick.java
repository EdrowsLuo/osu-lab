package com.edlplan.nso.ruleset.std.game.drawables;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.base.game.paint.AdvancedDrawObject;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.interpolate.EasingManager;

public class SliderTick extends AdvancedDrawObject {

    public final double TICK_ANIMATION_DURATION = 150;

    private final TextureQuad tick = new TextureQuad()
            .enableScale();

    public final Vec2 position = tick.position;

    public void initialPositon(Vec2 pos) {
        tick.position.set(pos);
    }

    public void initialTexture(AbstractTexture tickT) {
        tick.setTextureAndSize(tickT);
    }

    public void initialBaseScale(float scale) {
        tick.size.zoom(scale);
    }

    public void preemptAnimation(double time) {
        tick.alpha.value = 0;
        tick.scale.set(0.5f);
        addAnimTask(time, TICK_ANIMATION_DURATION, time1 -> tick.alpha.value = (float) time1);
        addAnimTask(time, TICK_ANIMATION_DURATION * 4, time1 -> tick.scale.set(
                FMath.linear(
                        (float) EasingManager.apply(Easing.OutElasticHalf, time1),
                        0.5f, 1f)));
    }

    public void missAnimation(double time) {
        addAnimTask(time, TICK_ANIMATION_DURATION, time1 -> tick.alpha.value = (float) (1 - time1));
    }

    public void hitAnimation(double time) {
        addAnimTask(time, TICK_ANIMATION_DURATION, time1 -> tick.alpha.value =
                FMath.linear(
                        (float) EasingManager.apply(Easing.OutQuint, time1),
                        tick.alpha.value, 0));
        addAnimTask(time, TICK_ANIMATION_DURATION, time1 -> tick.scale.set(
                FMath.linear(
                        (float) EasingManager.apply(Easing.Out, time1),
                        tick.scale.x, 1.5f)));
        addTask(time + TICK_ANIMATION_DURATION, this::detach);
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().add(tick);
    }
}
