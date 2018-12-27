package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.interpolate.EasingManager;
import com.edplan.framework.utils.FloatRef;
import com.edplan.nso.ruleset.base.game.World;

public class CirclePiece extends BasePiece {

    private final TextureQuad circle = new TextureQuad()
            .enableScale()
            .enableColor();

    private final TextureQuad overlay = new TextureQuad()
            .syncPosition(circle.position)
            .syncScale(circle.scale)
            .syncAlpha(circle.alpha);

    public final Vec2 position = circle.position;

    public final Vec2 scale = circle.scale;

    public final FloatRef alpha = circle.alpha;

    public CirclePiece() {

    }

    @Override
    public void expire(double time) {
        scale.set(1);
        alpha.value = 1;

        addAnimTask(time, 400, time1 -> scale.set(
                FMath.linear(
                        (float) EasingManager.apply(Easing.OutQuad, time1),
                        1, 1.5f)));
        addAnimTask(time, 800, time1 -> alpha.value = (float) (1 - time1));
        addTask(time + 800, this::detach);
    }

    public void initialFadeInAnim(double start, double duration) {
        alpha.value = 0;
        addAnimTask(start, duration, time -> alpha.value = (float) time);
    }

    public void initialTexture(AbstractTexture circleT, AbstractTexture overlayT) {
        circle.setTextureAndSize(circleT);
        overlay.setTextureAndSize(overlayT);
    }

    @Override
    public void initialBaseScale(float scale) {
        circle.size.zoom(scale);
        overlay.size.zoom(scale);
    }

    @Override
    public void initialAccentColor(Color4 color) {
        circle.accentColor.set(color);
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().addAll(circle, overlay);
    }
}
