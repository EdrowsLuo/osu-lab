package com.edplan.nso.ruleset.std.playing.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.nso.ruleset.base.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.std.playing.drawable.piece.FollowpointPiece;
import com.edplan.framework.ui.animation.AnimationHelper;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class DrawableStdFollowpoint extends DrawableStdHitObject {
    private FollowpointPiece followpointPiece;

    private DrawableStdHitObject obj1;

    private DrawableStdHitObject obj2;

    public DrawableStdFollowpoint(MContext c, DrawableStdHitObject obj1, DrawableStdHitObject obj2) {
        super(c, obj1.getHitObject());
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public void setObj1(DrawableStdHitObject obj1) {
        this.obj1 = obj1;
    }

    public DrawableStdHitObject getObj1() {
        return obj1;
    }

    public void setObj2(DrawableStdHitObject obj2) {
        this.obj2 = obj2;
    }

    public DrawableStdHitObject getObj2() {
        return obj2;
    }

    //followpoint实际上不代表任何HitObject
    @Override
    public int getObjStartTime() {

        return 0;
    }

    @Override
    public int getObjPredictedEndTime() {

        return 0;
    }

    @Override
    public Vec2 getStartPoint() {

        return obj1.getEndPoint();
    }

    @Override
    public Vec2 getEndPoint() {

        return obj2.getEndPoint();
    }

    @Override
    public void applyDefault(PlayingBeatmap beatmap) {

        super.applyDefault(beatmap);
        setShowTime(obj1.getObjPredictedEndTime() - getTimePreempt());
        followpointPiece = new FollowpointPiece(getContext(), beatmap.getTimeLine(), obj1.getEndPoint(), obj2.getStartPoint());
        applyPiece(followpointPiece, beatmap);
    }

    @Override
    public void draw(BaseCanvas canvas) {

        super.draw(canvas);
        followpointPiece.draw(canvas);
    }

    @Override
    public void onShow() {

        super.onShow();
        (new FollowpointMoveAnimation()).post(getTimeLine());
        (new FollowpointColor2FadeInAnimation()).post(getTimeLine());
    }

    public void checkEnd() {
        if (a1e && a2e && a3e && a4e) {
            finish();
        }
    }

    @Override
    public void onFinish() {

        super.onFinish();
        followpointPiece.onFinish();
    }

    boolean a1e = false;

    public class FollowpointMoveAnimation extends BasePreciseAnimation {

        public FollowpointMoveAnimation() {
            setStartTime(obj1.getShowTime());
            setDuration(obj2.getShowTime() - getStartTimeAtTimeline());
        }

        @Override
        protected void seekToTime(double p) {

            super.seekToTime(p);
            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            followpointPiece.setProgress(fp);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            a1e = true;
            (new FollowpointColor1Animation()).post(getTimeLine());
        }
    }

    boolean a2e = false;

    public class FollowpointColor1Animation extends BasePreciseAnimation {
        public FollowpointColor1Animation() {
            setStartTime(obj2.getShowTime());
            setDuration(obj2.getTimeFadein());
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            Color4 c = followpointPiece.getColor1().copyNew();
            c.a = 1 - fp;
            followpointPiece.setColor1(c);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            a2e = true;
            checkEnd();
        }
    }

    boolean a3e = false;

    public class FollowpointColor2FadeInAnimation extends BasePreciseAnimation {
        public FollowpointColor2FadeInAnimation() {
            setStartTime(obj2.getShowTime());
            setDuration(obj2.getTimeFadein());
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            Color4 c = followpointPiece.getColor2().copyNew();
            c.a = fp;
            followpointPiece.setColor2(c);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            a3e = true;
            (new FollowpointColor2FadeOutAnimation()).post(getTimeLine());
        }
    }

    boolean a4e = false;

    public class FollowpointColor2FadeOutAnimation extends BasePreciseAnimation {
        public FollowpointColor2FadeOutAnimation() {
            setStartTime(obj2.getShowTime() + obj2.getTimeFadein());
            setDuration(obj2.getTimeFadein());
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            Color4 c = followpointPiece.getColor2().copyNew();
            c.a = 1 - fp;
            followpointPiece.setColor2(c);
        }

        @Override
        public void onEnd() {

            super.onEnd();
            a4e = true;
            checkEnd();
        }
    }
}
