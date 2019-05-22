package com.edlplan.nso.ruleset.std.game.drawables;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.base.game.paint.AdvancedDrawObject;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.FloatRef;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdGameObject;

public class ApproachCircle extends AdvancedDrawObject {

    private final TextureQuad approachCircle = new TextureQuad()
            .enableColor()
            .enableScale();

    public final Vec2 position = approachCircle.position;

    public final Vec2 scale = approachCircle.scale;

    public final FloatRef alpha = approachCircle.alpha;

    public void initialApproachAnim(double start, double duration, double fadeInDuration) {
        scale.set(StdGameObject.APPROACH_CIRCLE_START_SCALE);
        alpha.value = 0;
        addAnimTask(start, fadeInDuration, time -> alpha.value = (float) time);
        addAnimTask(start, duration, time -> scale.set(
                FMath.linear(
                        (float) time,
                        StdGameObject.APPROACH_CIRCLE_START_SCALE,
                        StdGameObject.APPROACH_CIRCLE_END_SCALE)));
        addTask(start + duration, this::detach);
    }

    public void initialApproachCircleTexture(AbstractTexture texture) {
        approachCircle.setTextureAndSize(texture);
    }

    public void initialBaseScale(float scale) {
        approachCircle.size.zoom(scale);
    }

    public void initialAccentColor(Color4 color) {
        approachCircle.accentColor.set(color);
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().add(approachCircle);
    }
}
