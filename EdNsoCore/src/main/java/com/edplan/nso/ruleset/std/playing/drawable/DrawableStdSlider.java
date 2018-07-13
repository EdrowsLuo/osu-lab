package com.edplan.nso.ruleset.std.playing.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.base.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.std.objects.StdSlider;
import com.edplan.nso.ruleset.std.objects.drawables.StdSliderPathMaker;
import com.edplan.nso.ruleset.std.playing.drawable.piece.SliderBody;
import com.edplan.nso.ruleset.std.playing.drawable.piece.HitCirclePiece;
import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.ui.animation.AnimationHelper;
import com.edplan.nso.ruleset.std.playing.drawable.interfaces.IHasApproachCircle;
import com.edplan.nso.ruleset.std.playing.drawable.piece.ApproachCircle;
import com.edplan.nso.timing.TimingPoint;
import com.edplan.nso.ruleset.std.playing.controlpoint.TimingControlPoint;
import com.edplan.nso.ruleset.std.playing.drawable.piece.ComboIndexPiece;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class DrawableStdSlider extends DrawableStdHitObject implements IHasApproachCircle {
    private final double BASE_SCORING_DISTANCE = 100;

    private ApproachCircle approachCircle;

    private StdSlider slider;

    private LinePath path;

    private Vec2 endPoint;

    private SliderBody body;

    private double velocity;

    private HitCirclePiece startPiece;

    private ComboIndexPiece comboPiece;

    private boolean snakeOut = false;

    public DrawableStdSlider(MContext c, StdSlider slider) {
        super(c, slider);
        this.slider = slider;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    @Override
    public int getObjPredictedEndTime() {

        return (int) (slider.getStartTime() + slider.getRepeat() * slider.getPixelLength() / getVelocity());
    }

    @Override
    public ApproachCircle getApproachCircle() {

        return approachCircle;
    }

    public LinePath getPath() {
        return path;
    }

    @Override
    public Vec2 getEndPoint() {

        return endPoint.copy();
    }

    @Override
    public void applyDefault(PlayingBeatmap beatmap) {

        super.applyDefault(beatmap);

        TimingControlPoint timingPoint = beatmap.getControlPoints().getTimingPointAt(slider.getStartTime());
        double speedMultiplier = beatmap.getControlPoints().getDifficultyPointAt(slider.getStartTime()).getSpeedMultiplier();

        double scoringDistance = BASE_SCORING_DISTANCE * beatmap.getDifficulty().getSliderMultiplier() * speedMultiplier;
        velocity = scoringDistance / timingPoint.getBeatLength();

        StdSliderPathMaker maker = new StdSliderPathMaker(slider.getPath());
        maker.setBaseSize(getBaseSize());
        path = maker.calculatePath();

        path.measure();
        path.bufferLength((float) slider.getPixelLength());
        endPoint = (slider.getRepeat() % 2 == 1) ? path.getMeasurer().atLength((float) slider.getPixelLength()) : new Vec2(slider.getStartX(), slider.getStartY());

        body = new SliderBody(getContext(), beatmap.getTimeLine(), this);
        applyPiece(body, beatmap);

        startPiece = new HitCirclePiece(getContext(), beatmap.getTimeLine());
        startPiece.setAlpha(0);
        applyPiece(startPiece, beatmap);

        approachCircle = new ApproachCircle(getContext(), getTimeLine());
        applyPiece(approachCircle, beatmap);

        comboPiece = new ComboIndexPiece(getContext(), beatmap.getTimeLine(), getComboIndex());
        applyPiece(comboPiece, beatmap);
    }

    @Override
    public void onApplyStackHeight() {

        super.onApplyStackHeight();
        if (body != null) body.applyStackOffset(getStackOffset());
    }

    @Override
    public void draw(BaseCanvas canvas) {

        super.draw(canvas);
        body.draw(canvas);
        startPiece.draw(canvas);
        comboPiece.draw(canvas);
    }

    @Override
    public void onShow() {

        super.onShow();
        (new ShowSliderAnimation()).post(getTimeLine());
        approachCircle.fadeAndScaleIn(this);
    }

    @Override
    public boolean isFinished() {

        return super.isFinished() && startPiece.isFinished();
    }

    @Override
    public void onFinish() {

        super.onFinish();
        startPiece.onFinish();
        body.onFinish();
        approachCircle.onFinish();
    }

    public class ShowSliderAnimation extends BasePreciseAnimation {
        public ShowSliderAnimation() {
            setStartTime(getShowTime());
            setDuration(getTimeFadein());
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            startPiece.setAlpha(fp);
            comboPiece.setAlpha(fp);
            body.setAlpha(fp);
            body.setProgress2(fp);
        }

        @Override
        public void onFinish() {

            super.onFinish();
            (new SlideOutAnimation()).post(getTimeLine());
        }
    }

    public class SlideOutAnimation extends BasePreciseAnimation {
        public SlideOutAnimation() {
            setStartTime(getObjStartTime());
            setDuration(getObjPredictedEndTime() - getObjStartTime());
        }

        @Override
        public void onStart() {

            super.onStart();
            comboPiece.finish();
            //comboPiece.fadeOut(DrawableStdSlider.this,getObjStartTime());
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            float repeatProgress = (fp * slider.getRepeat()) % 1;
            int repeatCount = (int) (fp * slider.getRepeat());
            float positionProgress = ((repeatCount % 2 == 0) ? repeatProgress : (1 - repeatProgress));
            startPiece.setOrigin(body.getPointAt((float) (slider.getPixelLength() * positionProgress)));
            if (snakeOut) if (repeatCount == slider.getRepeat() - 1) {
                if (repeatCount % 2 == 0) {
                    body.setProgress1(positionProgress);
                } else {
                    body.setProgress2(positionProgress);
                }
            }
        }

        @Override
        public void onFinish() {

            super.onFinish();
            body.setAlpha(0);
            startPiece.explode(getObjPredictedEndTime(), DrawableStdSlider.this);
            finish();
            (new FadeOutAnimation()).post(getTimeLine());
        }
    }

    public class FadeOutAnimation extends BasePreciseAnimation {
        public FadeOutAnimation() {
            setStartTime(getObjPredictedEndTime());
            setDuration(getTimeFadein() / 2);
        }

        @Override
        protected void seekToTime(double p) {

            float fp = AnimationHelper.getFloatProgress(p, getDuration());
            //startPiece.setAlpha(1-fp);
            body.setAlpha(1 - fp);
        }

        @Override
        public void onFinish() {

            super.onFinish();
            body.finish();
        }
    }
}
