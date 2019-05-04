package com.edplan.nso.ruleset.std.objects.v2.direct;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.base.beatmap.controlpoint.TimingControlPoint;
import com.edplan.nso.ruleset.base.game.judge.AreaHoldObject;
import com.edplan.nso.ruleset.base.game.judge.HitArea;
import com.edplan.nso.ruleset.base.game.judge.HitWindow;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.StdScore;
import com.edplan.nso.ruleset.std.game.drawables.SliderTick;
import com.edplan.nso.ruleset.std.objects.drawables.StdSliderPathMaker;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdGameObject;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdSlider;

import java.util.ArrayList;
import java.util.List;

public class DirectStdSliderBody extends DirectStdGameObject {

    public final double BASE_SCORING_DISTANCE = 100;

    private LinePath path;

    private TimingControlPoint timingPoint;

    private int comboIndex;

    private double startTime;

    private double speedMultiplier;

    private double scoringDistance;

    private double tickDistance;

    private double velocity;

    private float length;

    private double spanDuration;

    private int repeat;

    private Vec2 startPosition, endPosition;

    private DirectStdSliderStartCircle startCircle;

    private DirectStdSliderEndCircle endCircle;

    private List<DirectStdSliderRepeatCircle> repeatCircles = new ArrayList<>();

    private List<DirectStdSliderTick> ticks = new ArrayList<>();

    public DirectStdSliderBody(StdSlider slider, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        super(beatmap, property);

        repeat = slider.getRepeat();

        timingPoint = getBeatmap().getControlPoints().getTimingPointAt(slider.getTime());
        speedMultiplier = getBeatmap().getControlPoints().getDifficultyPointAt(slider.getTime()).getSpeedMultiplier();

        scoringDistance = BASE_SCORING_DISTANCE * getBeatmap().getDifficulty().getSliderMultiplier() * speedMultiplier;
        velocity = scoringDistance / timingPoint.getBeatLength();
        tickDistance = scoringDistance / getBeatmap().getDifficulty().getSliderTickRate();
        spanDuration = slider.getPixelLength() / velocity;

        this.length = (float) slider.getPixelLength();
        this.startTime = slider.getTime();
        this.comboIndex = comboIndex;

        StdSliderPathMaker maker = new StdSliderPathMaker(slider.getStdPath());
        maker.setBaseSize((float) getSizeAfterCS() / 2);
        path = maker.calculatePath();

        path.measure();
        path.bufferLength(length);
        path = path.fitToLinePath();
        path.measure();

        startPosition = new Vec2(slider.getX(), slider.getY());
        endPosition = path.getMeasurer().atLength(length);

    }

    public void addNestedObjects(DirectStdGameObjectFactory factory) {
        float fixedLength = Math.min(100000, length);
        double tickDistance = FMath.clamp(this.tickDistance, 0, length);
        double minDistanceFromEnd = velocity * 10;

        startCircle = factory.createSliderStartCircle(this);
        endCircle = factory.createSliderEndCircle(this);
        if (tickDistance != 0) {
            for (int span = 0; span < repeat; span++) {
                boolean reversed = span % 2 == 1;
                if (span == repeat - 1) {
                    //是最后的一个
                }
                for (double d = tickDistance; d <= fixedLength; d += tickDistance) {
                    if (d >= fixedLength - minDistanceFromEnd) {
                        break;
                    }
                    double pathProgress = d / fixedLength;
                    double timeProgress = reversed ? (1 - pathProgress) : pathProgress;
                    if (ticks.size() > 500) {
                        //当一个slider里有超过500个tick的时候我们不继续添加tick
                        break;
                    }
                    ticks.add(factory.createTick(
                            this,
                            pathProgress,
                            span,
                            timeProgress));
                }
            }
        }
    }

    public int getComboIndex() {
        return comboIndex;
    }

    public Vec2 getStartPosition() {
        return startPosition;
    }

    public Vec2 getEndPosition() {
        return endPosition;
    }

    public int getRepeat() {
        return repeat;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return getStartTime() + getSpanDuration() * getRepeat();
    }

    public DirectStdSliderStartCircle getStartCircle() {
        return startCircle;
    }

    public float getLength() {
        return length;
    }

    public LinePath getPath() {
        return path;
    }

    public double getScoringDistance() {
        return scoringDistance;
    }

    public double getSpanDuration() {
        return spanDuration;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getVelocity() {
        return velocity;
    }

    public DirectStdSliderEndCircle getEndCircle() {
        return endCircle;
    }

    public List<DirectStdSliderRepeatCircle> getRepeatCircles() {
        return repeatCircles;
    }

    public List<DirectStdSliderTick> getTicks() {
        return ticks;
    }

    public TimingControlPoint getTimingPoint() {
        return timingPoint;
    }

}
