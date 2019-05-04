package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.resource.Skin;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.interpolate.EasingManager;
import com.edplan.framework.utils.FloatRef;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.build.BuildContext;
import com.edplan.nso.ruleset.base.game.build.Style;
import com.edplan.nso.ruleset.base.game.paint.AdvancedDrawObject;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.game.StdGameField;

public class CirclePiece extends AdvancedDrawObject {

    private final TextureQuad circle = new TextureQuad()
            .enableScale();

    private final TextureQuad overlay = new TextureQuad()
            .syncPosition(circle.position)
            .syncScale(circle.scale)
            .syncAlpha(circle.alpha);

    public Vec2 position = circle.position;

    public Vec2 scale = circle.scale;

    public FloatRef alpha = circle.alpha;

    public CirclePiece() {

    }

    public void expire(double time) {
        scale.set(1);
        alpha.value = 1;

        addAnimTask(time, 200, time1 -> scale.set(
                FMath.linear(
                        (float) EasingManager.apply(Easing.OutQuad, time1),
                        1, 1.5f)));
        addAnimTask(time, 200, time1 -> alpha.value = (float) (1 - time1));
        addTask(time + 200, this::detach);
        detach();
    }

    public void initialFadeInAnim(double start, double duration) {
        alpha.value = 0;
        addAnimTask(start, duration, time -> alpha.value = (float) time);
    }

    public void initialTexture(AbstractTexture circleT, AbstractTexture overlayT) {
        circle.setTextureAndSize(circleT);
        overlay.setTextureAndSize(overlayT);
    }

    public void initialBaseScale(float scale) {
        circle.size.zoom(scale);
        overlay.size.zoom(scale);
    }

    public void initialAccentColor(Color4 color) {
        circle.enableColor().accentColor.set(color);
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        TextureQuadBatch.getDefaultBatch().addAll(circle, overlay);
    }


    public static final String KEY_TEXTURE_HIT_CIRCLE = "TEXTURE_HIT_CIRCLE";

    public static final String KEY_TEXTURE_HIT_CIRCLE_OVERLAY = "TEXTURE_HIT_CIRCLE_OVERLAY";

    public static final Style<CirclePiece> DefaultStyle = new Style<CirclePiece>() {

        @Override
        protected void initial(Style<? extends CirclePiece> style) {
            style.data(KEY_TEXTURE_HIT_CIRCLE, StdSkin.hitcircle);
            style.data(KEY_TEXTURE_HIT_CIRCLE_OVERLAY, StdSkin.hitcircleoverlay);
        }

        @Override
        public void apply(CirclePiece circlePiece, BuildContext context) {
            StdGameField gameField = context.getProperty(StdGameField.KEY_STD_GAME_FIELD);
            Skin skin = context.getSkin();
            circlePiece.initialTexture(
                    skin.getTexture(getString(KEY_TEXTURE_HIT_CIRCLE)),
                    skin.getTexture(getString(KEY_TEXTURE_HIT_CIRCLE_OVERLAY)));
            circlePiece.initialBaseScale(gameField.globalScale);
        }
    };

    public static final Style<CirclePiece> SliderStartStyle = DefaultStyle.override(
            style -> {
                style.data(KEY_TEXTURE_HIT_CIRCLE, StdSkin.sliderstartcircle);
                style.data(KEY_TEXTURE_HIT_CIRCLE_OVERLAY, StdSkin.sliderstartcircleoverlay);
            }
    );

    public static final Style<CirclePiece> SliderEndStyle = DefaultStyle.override(
            style -> {
                style.data(KEY_TEXTURE_HIT_CIRCLE, StdSkin.sliderendcircle);
                style.data(KEY_TEXTURE_HIT_CIRCLE_OVERLAY, StdSkin.sliderendcircleoverlay);
            }
    );
}
