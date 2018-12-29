package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.base.game.judge.HitArea;
import com.edplan.nso.ruleset.base.game.judge.HitWindow;
import com.edplan.nso.ruleset.base.game.judge.PositionHitObject;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edplan.nso.ruleset.std.game.drawables.SliderBody;
import com.edplan.nso.ruleset.std.objects.drawables.StdSliderPathMaker;
import com.edplan.nso.ruleset.std.objects.v2.StdGameObject;
import com.edplan.nso.ruleset.std.objects.v2.StdSlider;
import com.edplan.nso.ruleset.std.playing.controlpoint.TimingControlPoint;

public class WorkingStdSlider extends WorkingStdGameObject<StdSlider> {

    public final double BASE_SCORING_DISTANCE = 100;

    private LinePath path;

    private Vec2 endPoint;

    private double velocity;

    public WorkingStdSlider(StdSlider gameObject, StdBeatmap beatmap) {
        super(gameObject, beatmap);

    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    @Override
    public double getEndTime() {
        StdSlider slider = getGameObject();
        return slider.getTime() + slider.getRepeat() * slider.getPixelLength() / getVelocity();
    }

    @Override
    public Vec2 getEndPosition() {
        return endPoint.copy();
    }

    @Override
    public void applyToGameField(StdGameField gameField) {
        StdBeatmap beatmap = gameField.beatmap;
        StdSlider slider = getGameObject();
        TimingControlPoint timingPoint = beatmap.getControlPoints().getTimingPointAt(slider.getTime());
        double speedMultiplier = beatmap.getControlPoints().getDifficultyPointAt(slider.getTime()).getSpeedMultiplier();

        double scoringDistance = BASE_SCORING_DISTANCE * beatmap.getDifficulty().getSliderMultiplier() * speedMultiplier;
        velocity = scoringDistance / timingPoint.getBeatLength();

        StdSliderPathMaker maker = new StdSliderPathMaker(slider.getStdPath());
        maker.setBaseSize(StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);
        path = maker.calculatePath();

        path.measure();
        path.bufferLength((float) slider.getPixelLength());
        endPoint = (slider.getRepeat() % 2 == 1) ? path.getMeasurer().atLength((float) slider.getPixelLength()) : new Vec2(getStartPosition());







        CirclePiece circlePiece = new CirclePiece();
        circlePiece.initialTexture(
                gameField.skin.getTexture(StdSkin.sliderstartcircle),
                gameField.skin.getTexture(StdSkin.sliderstartcircleoverlay));
        circlePiece.initialBaseScale(gameField.globalScale);
        circlePiece.initialFadeInAnim(
                getGameObject().getTime() - getGameObject().getTimePreempt(gameField.beatmap),
                getGameObject().getFadeInDuration(gameField.beatmap));
        circlePiece.position.set(getGameObject().getX(), getGameObject().getY());

        ApproachCircle approachCircle = new ApproachCircle();
        approachCircle.initialApproachCircleTexture(gameField.skin.getTexture(StdSkin.approachcircle));
        approachCircle.initialBaseScale(gameField.globalScale);
        approachCircle.initialApproachAnim(
                getGameObject().getTime() - getGameObject().getTimePreempt(gameField.beatmap),
                getGameObject().getTimePreempt(gameField.beatmap),
                getGameObject().getTimePreempt(gameField.beatmap));
        approachCircle.position.set(getGameObject().getX(), getGameObject().getY());

        gameField.hitobjectLayer.scheduleAttachBehind(getShowTime(), circlePiece);
        gameField.approachCircleLayer.scheduleAttachBehind(getShowTime(), approachCircle);





        SliderBody body = new SliderBody(
                gameField.getContext(),
                path.cutPath(0, (float) slider.getPixelLength()),
                StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale,
                (float) slider.getPixelLength());

        body.addAnimTask(getShowTime(), slider.getFadeInDuration(beatmap), t -> body.setProgress2((float) t));

        gameField.sliderLayer.scheduleAttachBehindAll(getShowTime(), body);
        gameField.sliderLayer.addEvent(getEndTime(), body::detach);

        PositionHitObject positionHitObject = new PositionHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(
                    getGameObject().getTime(),
                    gameField.difficultyHelper.hitWindowFor50(gameField.beatmap.getDifficulty().getOverallDifficulty())
            );

            area = HitArea.circle(getGameObject().getX(), getGameObject().getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);

            onHit = (time, x, y) -> {
                circlePiece.postOperation(() -> {
                    approachCircle.expire(time);
                    circlePiece.expire(time);
                });
            };

            onTimeOut = time -> {
                circlePiece.postOperation(() -> {
                    approachCircle.detach();
                    circlePiece.detach();
                    gameField.addHitEffect(
                            StdGameObject.HitLevel.MISS,
                            time,
                            getGameObject().getX(), getGameObject().getY(),
                            gameField.globalScale,
                            gameField.skin);
                });
            };

        }};

        gameField.world.getJudgeWorld().addJudgeObject(positionHitObject);
    }
}
