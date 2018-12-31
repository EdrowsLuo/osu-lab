package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
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

    public double getCycleTime() {
        return getGameObject().getPixelLength() / getVelocity();
    }

    @Override
    public double getEndTime() {
        StdSlider slider = getGameObject();
        return slider.getTime() + slider.getRepeat() * getCycleTime();
    }

    @Override
    public Vec2 getEndPosition() {
        return endPoint.copy();
    }

    static int id = 0;
    @Override
    public void applyToGameField(StdGameField gameField) {
        id++;
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
                getGameObject().getTime() - getTimePreempt(),
                getGameObject().getFadeInDuration(gameField.beatmap));
        circlePiece.position.set(getGameObject().getX(), getGameObject().getY());

        SliderBody body = new SliderBody(
                gameField.getContext(),
                path.cutPath(0, (float) slider.getPixelLength()),
                StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale,
                (float) slider.getPixelLength());



        CirclePiece endCircle = new CirclePiece() {
            @Override
            protected void onDraw(BaseCanvas canvas, World world) {
                position.set(body.getEndPosition());
                super.onDraw(canvas, world);
            }
        };
        endCircle.initialTexture(
                gameField.skin.getTexture(StdSkin.sliderendcircle),
                gameField.skin.getTexture(StdSkin.sliderendcircleoverlay));
        endCircle.initialBaseScale(gameField.globalScale);
        endCircle.initialFadeInAnim(
                getGameObject().getTime() - getTimePreempt(),
                getGameObject().getFadeInDuration(gameField.beatmap));
        endCircle.initialAccentColor(Color4.Red);
        
        ApproachCircle approachCircle = gameField.buildApprochCircle(getGameObject());

        gameField.hitobjectLayer.scheduleAttachBehindAll(getShowTime(), circlePiece, endCircle);
        gameField.hitobjectLayer.addEvent(slider.getTime(), circlePiece::detach);
        gameField.approachCircleLayer.scheduleAttachBehind(getShowTime(), approachCircle);


        body.addAnimTask(getShowTime(), slider.getFadeInDuration(beatmap),
                t -> {
                    body.setProgress2((float) t);
                    body.alpha.value = (float) t;
                });
        body.addAnimTask(
                getEndTime(),
                200,
                time -> body.alpha.value = (float) (1 - time)
        );

        if (id == 12) {
            System.out.println("slider12:: " + endCircle.position);
            System.out.println(body.getPath().getAll());
            System.out.println(slider.getPixelLength());
            System.out.println(slider.getStdPath().getControlPoints());
        }

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
        gameField.hitobjectLayer.addEvent(getEndTime(), () -> endCircle.expire(getEndTime()));
        gameField.sliderLayer.addEvent(getEndTime() + 200, () -> {
            body.detach();
            ball.detach();
            endCircle.detach();
        });

        PositionHitObject positionHitObject = new PositionHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(getGameObject().getTime(), gameField.difficultyHelper.hitWindowFor50());

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
                });
            };

        }};

        gameField.world.getJudgeWorld().addJudgeObject(positionHitObject);
    }
}
