package com.edlplan.nso.ruleset.std.game;

import com.edlplan.nso.ruleset.base.beatmap.controlpoint.TimingControlPoint;
import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.base.game.judge.AreaHitObject;
import com.edlplan.nso.ruleset.base.game.judge.AreaHoldObject;
import com.edlplan.nso.ruleset.base.game.judge.HitArea;
import com.edlplan.nso.ruleset.base.game.judge.HitWindow;
import com.edlplan.nso.ruleset.std.StdSkin;
import com.edlplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edlplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edlplan.nso.ruleset.std.game.drawables.SliderBall;
import com.edlplan.nso.ruleset.std.game.drawables.SliderBody;
import com.edlplan.nso.ruleset.std.game.drawables.SliderReverseCircle;
import com.edlplan.nso.ruleset.std.game.drawables.SliderTailCircle;
import com.edlplan.nso.ruleset.std.game.drawables.SliderTick;
import com.edlplan.nso.ruleset.std.objects.drawables.StdSliderPathMaker;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdGameObject;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdSlider;
import com.edlplan.framework.graphics.line.LinePath;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.Vec2;

import java.util.ArrayList;
import java.util.List;

public class WorkingStdSlider extends WorkingStdGameObject<StdSlider> {

    public static final float BASE_HIT_AREA_RADIUS = StdGameObject.BASE_OBJECT_SIZE * 0.75f;

    public final double BASE_SCORING_DISTANCE = 100;

    private double bodyFadeOutTime = 200;

    private LinePath path;

    private Vec2 endPoint;

    private double velocity;

    StdSlider slider;

    TimingControlPoint timingPoint;

    double speedMultiplier;

    double scoringDistance;

    double tickDistance;

    public WorkingStdSlider(StdSlider gameObject, StdGameField gameField) {
        super(gameObject, gameField);
        slider = getGameObject();
        timingPoint = getBeatmap().getControlPoints().getTimingPointAt(slider.getTime());
        speedMultiplier = getBeatmap().getControlPoints().getDifficultyPointAt(slider.getTime()).getSpeedMultiplier();

        scoringDistance = BASE_SCORING_DISTANCE * getBeatmap().getDifficulty().getSliderMultiplier() * speedMultiplier;
        velocity = scoringDistance / timingPoint.getBeatLength();
        tickDistance = scoringDistance / getBeatmap().getDifficulty().getSliderTickRate();

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

    @Override
    public void applyToGameField() {
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

        //----------------------------------repeat circles------------------------------------------------------
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
                Vec2 endPos = (ii % 2 == 0) ? body.getBodyTailPosition() : body.getStartPosition();
                SliderTailCircle circle = new SliderTailCircle() {
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

                AreaHoldObject areaHitObject = new AreaHoldObject() {{

                    hitWindow = HitWindow.interval(getEndTime(), gameField.difficultyHelper.hitWindowFor50());

                    area = HitArea.circle(circle.position, BASE_HIT_AREA_RADIUS * gameField.globalScale);

                    onHit = (time, x, y) -> circle.postOperation(() -> circle.expire(time));

                    onTimeOut = (time) -> circle.postOperation(() -> {
                        circle.detach();
                        gameField.addHitEffect(
                                StdScore.HitLevel.MISS,
                                time,
                                endPos.x, endPos.y,
                                gameField.globalScale,
                                gameField.skin);
                    });

                }};

                gameField.world.getJudgeWorld().addJudgeObject(areaHitObject);

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

                AreaHoldObject areaHitObject = new AreaHoldObject() {{

                    hitWindow = HitWindow.interval(hitTime, gameField.difficultyHelper.hitWindowFor50());

                    area = HitArea.circle(circle.position, BASE_HIT_AREA_RADIUS * gameField.globalScale);

                    onHit = (time, x, y) -> circle.postOperation(() -> circle.expire(time));

                    onTimeOut = (time) -> circle.postOperation(() -> {
                        circle.detach();
                        gameField.addHitEffect(
                                StdScore.HitLevel.MISS,
                                time,
                                circle.position.x, circle.position.y,
                                gameField.globalScale,
                                gameField.skin);
                    });

                }};

                gameField.world.getJudgeWorld().addJudgeObject(areaHitObject);
            }

        }
        
        //--------------------------slider ticks----------------------------------------------

        List<SliderTick> sliderTicks = new ArrayList<>();

        float length = Math.min(100000, body.getLength());
        double tickDistance = FMath.clamp(this.tickDistance, 0, length);
        double minDistanceFromEnd = velocity * 10;
        final int repeat = getGameObject().getRepeat();
        double startTime = getStartTime();
        double spanDuration = getGameObject().getPixelLength() / velocity;

        /*System.out.println("slider repeat=" + repeat
                + " tickDistance=" + tickDistance + "," + this.tickDistance
                + " fromEnd=" + minDistanceFromEnd
                + " length=" + length);*/


        if (tickDistance != 0) {
            for (int span = 0; span < repeat; span++) {
                double spanStartTime = startTime + span * spanDuration;
                boolean reversed = span % 2 == 1;

                for (double d = tickDistance; d <= length; d += tickDistance) {
                    if (d >= length - minDistanceFromEnd) {
                        break;
                    }
                    double pathProgress = d / length;
                    double timeProgress = reversed ? (1 - pathProgress) : pathProgress;
                    double time = spanStartTime + timeProgress * spanDuration;
                    double appearTime = spanStartTime - getTimePreempt();

                    SliderTick tick = new SliderTick();
                    tick.position.set(body.getPointAtProgress((float) pathProgress));
                    tick.initialTexture(gameField.skin.getTexture(StdSkin.sliderscorepoint));
                    tick.initialBaseScale(gameField.globalScale);

                    tick.preemptAnimation(appearTime);
                    gameField.hitobjectLayer.scheduleAttachBehind(appearTime, tick);
                    //System.out.println("add tick at " + tick.position + " " + time);

                    AreaHoldObject areaHitObject = new AreaHoldObject() {{

                        hitWindow = HitWindow.interval(time, gameField.difficultyHelper.hitWindowFor50());

                        area = HitArea.circle(tick.position, BASE_HIT_AREA_RADIUS * gameField.globalScale);

                        onHit = (time, x, y) -> tick.postOperation(() -> tick.hitAnimation(time));

                        onTimeOut = (time) -> tick.postOperation(() -> {
                            tick.detach();
                            gameField.addHitEffect(
                                    StdScore.HitLevel.MISS,
                                    time,
                                    tick.position.x, tick.position.y,
                                    gameField.globalScale,
                                    gameField.skin);
                        });

                    }};

                    gameField.world.getJudgeWorld().addJudgeObject(areaHitObject);

                }
            }
        }


        gameField.hitobjectLayer.scheduleAttachBehindAll(getShowTime(), circlePiece);
        gameField.hitobjectLayer.addEvent(slider.getTime(), circlePiece::detach);

        ApproachCircle approachCircle = gameField.buildApprochCircle(getGameObject());
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

        AreaHitObject areaHitObject = new AreaHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(getGameObject().getTime(), gameField.difficultyHelper.hitWindowFor50());

            area = HitArea.circle(getGameObject().getX(), getGameObject().getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);

            onHit = (time, x, y) -> {
                gameField.hitobjectLayer.postOperation(() -> {
                    approachCircle.detach();
                    if (circlePiece.isAttached()) {
                        circlePiece.expire(time);
                    }
                });
            };

            onTimeOut = time -> {

            };

        }};

        gameField.world.getJudgeWorld().addJudgeObject(areaHitObject);
    }
}
