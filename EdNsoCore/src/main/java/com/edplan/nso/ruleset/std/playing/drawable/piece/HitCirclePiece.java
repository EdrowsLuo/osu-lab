package com.edplan.nso.ruleset.std.playing.drawable.piece;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.FMath;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.framework.ui.drawable.interfaces.IFadeable;
import com.edplan.framework.ui.drawable.interfaces.IScaleable2D;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class HitCirclePiece extends BasePiece implements IScaleable2D, IFadeable {
    private GLTexture hitcircle;

    private GLTexture hitcircleOverlay;

    public HitCirclePiece(MContext c, PreciseTimeline timeline) {
        super(c, timeline);
    }

    @Override
    public void setSkin(OsuSkin skin) {
        super.setSkin(skin);
        hitcircle = skin.hitcircle.getRes();
        hitcircleOverlay = skin.hitcircleOverlay.getRes();
    }

    @Override
    public void draw(BaseCanvas canvas) {

        //将note绘制上去
        if (isFinished()) return;
        simpleDrawWithAccentColor(hitcircle, canvas);
        simpleDraw(hitcircleOverlay, canvas);
    }

    public void fadeIn(DrawableStdHitObject obj) {
        (new FadeInAnimation(obj)).post(getTimeline());
    }

    public void explode(int startTime, DrawableStdHitObject obj) {
        (new ExplodeAnimation(startTime, obj.getTimeFadein() / 2)).post(getTimeline());
    }

    public class ExplodeAnimation extends BasePreciseAnimation {
        public ExplodeAnimation(int startTime, int duration) {
            setStartTime(startTime);
            setDuration(duration);
            //(int)(m*(DrawableStdHitCircle.this.getHitObject().getStartTime()-getStartTimeAtTimeline())));
        }

        @Override
        protected void seekToTime(double p) {

            float fp = Math.max(0, (float) (p / getDuration()));
            fp = 1 - fp;
            float a = fp;
            setAlpha(a);
            float s = 1.0f * fp + 1.3f * (1 - fp);
            setScale(s, s);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            finish();
        }
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
            //Log.v("test_anim","end:"+getStartTimeAtTimeline());
            explode(obj.getHitObject().getStartTime(), obj);
        }
    }
}
