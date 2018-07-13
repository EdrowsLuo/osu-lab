package com.edplan.nso.ruleset.std.playing.drawable.piece;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.math.FMath;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.framework.ui.text.font.DrawString;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class ComboIndexPiece extends BasePiece {
    private final float BASE_TEXT_SIZE = 40;

    private float baseTextSize = BASE_TEXT_SIZE;

    private float textSize;

    private DrawString drawString;

    private int comboIndex;

    public ComboIndexPiece(MContext c, PreciseTimeline l, int comboIndex) {
        super(c, l);
        this.comboIndex = comboIndex;
    }

    @Override
    public void setBaseSize(float baseSize) {

        super.setBaseSize(baseSize);
        baseTextSize *= baseSize / BasePiece.DEF_SIZE;
        textSize = baseTextSize * getScale().x;
    }

    @Override
    public void setScale(float sx, float sy) {

        super.setScale(sx, sy);
        textSize = baseTextSize * sx;
    }

    @Override
    public void setSkin(OsuSkin skin) {

        super.setSkin(skin);
        drawString = new DrawString("" + comboIndex, skin.defaultTextureFont.getPool(), baseTextSize);
    }

    @Override
    public void draw(BaseCanvas canvas) {

        if (!isFinished()) {
            drawString.setHeight(textSize);
            drawString.drawToCanvas(canvas, paint, getOrigin(), DrawString.Alignment.Center);
        }
    }

    public void fadeIn(DrawableStdHitObject obj) {
        (new FadeInAnimation(obj)).post(getTimeline());
    }

    public void fadeOut(DrawableStdHitObject obj, int time) {
        (new FadeOutAnimation(obj, time)).post(getTimeline());
    }

    public class FadeInAnimation extends BasePreciseAnimation {
        DrawableStdHitObject obj;

        public FadeInAnimation(DrawableStdHitObject obj) {
            this.obj = obj;
            setDuration(obj.getTimeFadein());
            setStartTime(obj.getShowTime());
            setProgressTime(0);
        }

        @Override
        protected void seekToTime(double p) {

            float fp = (float) (p / getDuration());
            setAlpha(FMath.sin(fp * FMath.PiHalf));
        }

        @Override
        public void onEnd() {

            super.onEnd();
            fadeOut(obj, obj.getObjStartTime());
            //(new FadeOutAnimation(obj,obj.getObjStartTime())).post(getTimeline());
        }
    }

    public class FadeOutAnimation extends BasePreciseAnimation {
        DrawableStdHitObject obj;

        public FadeOutAnimation(DrawableStdHitObject obj, int startTime) {
            this.obj = obj;
            setDuration(obj.getTimeFadein() / 2);
            setStartTime(startTime);
            setProgressTime(0);
        }

        @Override
        protected void seekToTime(double p) {

            float fp = (float) (p / (float) getDuration());
            setAlpha(1 - fp);
            float s = 1.0f * (1 - fp) + 1.5f * fp;
            setScale(s, s);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            finish();
        }
    }
}
