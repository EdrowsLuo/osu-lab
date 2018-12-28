package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
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


    }
}
