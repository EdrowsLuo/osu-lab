package com.edplan.nso.ruleset.std.playing.drawable.piece;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.FMath;
import com.edplan.framework.ui.animation.AnimationHelper;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class ApproachCircle extends BasePiece {
    private GLTexture approachCircle;

    public ApproachCircle(MContext c, PreciseTimeline l) {
        super(c, l);
    }

    @Override
    public void setSkin(OsuSkin skin) {

        super.setSkin(skin);
        approachCircle = skin.approachCircle.getRes();
    }

    @Override
    public void draw(BaseCanvas canvas) {

        if (isFinished()) return;
        simpleDrawWithAccentColor(approachCircle, canvas);
    }

    public void fadeAndScaleIn(DrawableStdHitObject obj) {
        (new PreemptAnimation(obj)).post(getTimeline());
        (new FadeInAnimation(obj)).post(getTimeline());
    }

    public class PreemptAnimation extends BasePreciseAnimation {

        public PreemptAnimation(DrawableStdHitObject obj) {
            setDuration(obj.getTimePreempt());
            setStartTime(obj.getShowTime());
            setProgressTime(0);
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            //p/(float)getDuration();
            float s = 4f * (1 - fp) + 1.0f * fp;
            setScale(s, s);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            finish();
        }
    }

    public class FadeInAnimation extends BasePreciseAnimation {

        public FadeInAnimation(DrawableStdHitObject obj) {
            setDuration(obj.getTimeFadein());
            setStartTime(obj.getShowTime());
            setProgressTime(0);
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            //p/(float)getDuration();
            setAlpha(fp);
        }
    }
}
