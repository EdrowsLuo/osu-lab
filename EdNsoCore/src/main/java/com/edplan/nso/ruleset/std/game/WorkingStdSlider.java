package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.HitArea;
import com.edplan.nso.ruleset.base.game.judge.HitWindow;
import com.edplan.nso.ruleset.base.game.judge.PositionHitObject;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edplan.nso.ruleset.std.game.drawables.SliderBall;
import com.edplan.nso.ruleset.std.game.drawables.SliderBody;
import com.edplan.nso.ruleset.std.game.drawables.SliderReverseCircle;
import com.edplan.nso.ruleset.std.objects.drawables.StdSliderPathMaker;
import com.edplan.nso.ruleset.std.objects.v2.StdGameObject;
import com.edplan.nso.ruleset.std.objects.v2.StdSlider;
import com.edplan.nso.ruleset.std.playing.controlpoint.TimingControlPoint;

public class WorkingStdSlider extends WorkingStdGameObject<StdSlider> {

    public final double BASE_SCORING_DISTANCE = 100;

    private double bodyFadeOutTime = 200;

    private LinePath path;

    private Vec2 endPoint;

    private double velocity;

    StdSlider slider;

    TimingControlPoint timingPoint;

    double speedMultiplier;

    double scoringDistance;

    public WorkingStdSlider(StdSlider gameObject, StdGameField gameField) {
        super(gameObject, gameField);
        slider = getGameObject();
        timingPoint = getBeatmap().getControlPoints().getTimingPointAt(slider.getTime());
        speedMultiplier = getBeatmap().getControlPoints().getDifficultyPointAt(slider.getTime()).getSpeedMultiplier();

        scoringDistance = BASE_SCORING_DISTANCE * getBeatmap().getDifficulty().getSliderMultiplier() * speedMultiplier;
        velocity = scoringDistance / timingPoint.getBeatLength();

        StdSliderPathMaker maker = new StdSliderPathMaker(slider.getStdPath());
        maker.setBaseSize(StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);
        path = maker.calculatePath();

        path.measure();
        path.bufferLength((float) slider.getPixelLength());
        endPoint = (slider.getRepeat() % 2 == 1) ? path.getMeasurer().atLength((float) slider.getPixelLength()) : new Vec2(getStartPosition());

    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getCycleTime() {
        return getGameObject().getPixelLength() / getVelocity();
    }

    @Override
    public double getEndTime() {
        StdSlider slider = getGameObject();
        return slider.getTime() + slider.getRepeat() * getCycleTime();
    }

    @Override
    public double getHideTime() {
        return getEndTime() + bodyFadeOutTime;
    }

    @Override
    public Vec2 getEndPosition() {
        return endPoint.copy();
    }

    static int id = 0;
    @Override
    public void applyToGameField() {
        id++;
        StdGameField gameField = getGameField();
        CirclePiece circlePiece = new CirclePiece() {{

            SliderStartStyle.apply(this, gameField.buildContext);

            position.set(getGameObject().getX(), getGameObject().getY());

            initialFadeInAnim(
                    getShowTime(),
                    getFadeInDuration());

        }};

        SliderBody body = new SliderBody(
                gameField.getContext(),
                path.cutPath(0, (float) slider.getPixelLength()),
                StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale,
                (float) slider.getPixelLength());

        CirclePiece[] pieces = new CirclePiece[slider.getRepeat()];
        for (int i = 0; i < pieces.length; i++) {
            final int ii = i;
            double showTime = slider.getTime() + getCycleTime() * (i - 1);
            double hitTime = slider.getTime() + getCycleTime() * (i + 1);
            if (i == 0) {
                showTime = getShowTime();
            }

            if (i == pieces.length - 1) {
                //最后的一个circle不需要反转
                CirclePiece circle = new CirclePiece() {
                    @Override
                    protected void onDraw(BaseCanvas canvas, World world) {
                        position.set((ii % 2 == 0) ? body.getEndPosition() : body.getStartPosition());
                        super.onDraw(canvas, world);
                    }
                };
                circle.initialTexture(
                        gameField.skin.getTexture(StdSkin.sliderendcircle),
                        gameField.skin.getTexture(StdSkin.sliderendcircleoverlay));
                circle.initialBaseScale(gameField.globalScale);

                gameField.hitobjectLayer.scheduleAttachBehind(showTime, circle);
                circle.expire(getEndTime());
                gameField.hitobjectLayer.addEvent(getEndTime() + 200, circle::detach);

                pieces[i] = circle;
            } else if (i != pieces.length - 1) {
                SliderReverseCircle circle = new SliderReverseCircle() {
                    @Override
                    protected void onDraw(BaseCanvas canvas, World world) {
                        reverseArrow.rotation.value = (ii % 2 == 0) ? (body.getEndRotation() + FMath.Pi) : body.getStartRotation();
                        position.set((ii % 2 == 0) ? body.getEndPosition() : body.getStartPosition());
                        super.onDraw(canvas, world);
                    }
                };
                circle.initialTexture(
                        gameField.skin.getTexture(StdSkin.sliderendcircle),
                        gameField.skin.getTexture(StdSkin.sliderendcircleoverlay));
                circle.initialReverseTexture(
                        gameField.skin.getTexture(StdSkin.reversearrow)
                );
                circle.initialBaseScale(gameField.globalScale);

                gameField.hitobjectLayer.scheduleAttachBehind(showTime, circle);
                circle.expire(hitTime);
                gameField.hitobjectLayer.addEvent(hitTime + 200, circle::detach);
            }

        }
        
        ApproachCircle approachCircle = gameField.buildApprochCircle(getGameObject());

        gameField.hitobjectLayer.scheduleAttachBehindAll(getShowTime(), circlePiece);
        gameField.hitobjectLayer.addEvent(slider.getTime(), circlePiece::detach);
        gameField.approachCircleLayer.scheduleAttachBehind(getShowTime(), approachCircle);


        body.addAnimTask(getShowTime(), slider.getFadeInDuration(getBeatmap()),
                t -> {
                    body.setProgress2((float) t);
                    body.alpha.value = (float) t;
                });
        body.addAnimTask(
                getEndTime(),
                bodyFadeOutTime,
                time -> body.alpha.value = (float) (1 - time)
        );

        SliderBall ball = new SliderBall(body.getPath(), body.getLength());
        ball.sprite.alpha = body.alpha;
        ball.sprite.initialWithTextureListWithScale(
                gameField.skin.getTextureList(StdSkin.sliderb),
                gameField.globalScale
        );
        ball.setStartTime(slider.getTime());
        ball.setCycleDuration(600);
        ball.buildAnimation(
                slider.getTime(),
                getCycleTime(),
                slider.getRepeat()
        );

        gameField.sliderLayer.scheduleAttachBehindAll(getShowTime(), body);
        gameField.hitobjectLayer.scheduleAttachBehind(slider.getTime(), ball);
        gameField.sliderLayer.addEvent(getEndTime(), ball::detach);
        gameField.sliderLayer.addEvent(getHideTime(), body::detach);

        PositionHitObject positionHitObject = new PositionHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(getGameObject().getTime(), gameField.difficultyHelper.hitWindowFor50());

            area = HitArea.circle(getGameObject().getX(), getGameObject().getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);

            onHit = (time, x, y) -> {
                gameField.hitobjectLayer.postOperation(() -> {
                    if (circlePiece.isAttached()) {
                        approachCircle.detach();
                        circlePiece.expire(time);
                    }
                });
            };

            onTimeOut = time -> {

            };

        }};

        gameField.world.getJudgeWorld().addJudgeObject(positionHitObject);
    }
}
