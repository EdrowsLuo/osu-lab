package com.edplan.nso.ruleset.std.objects.v2.direct;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.difficulty.DifficultyUtil;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;

public class DirectStdSliderTick extends DirectStdGameObject {

    private DirectStdSliderBody sliderBody;

    private double positionProgress;

    private int repeat;

    private double timeOffset;

    private double size;

    /**
     *
     * @param sliderBody
     * @param positionProgress the position progress of the body path
     * @param repeat this tick belong to which repeat
     * @param timeOffset relative to repeat start time
     */
    public DirectStdSliderTick(
            DirectStdSliderBody sliderBody,
            double positionProgress,
            int repeat,
            double timeOffset) {
        super(sliderBody.getBeatmap(), sliderBody.getProperty());
        this.sliderBody = sliderBody;
        this.positionProgress = positionProgress;
        this.repeat = repeat;
        this.timeOffset = timeOffset;
        this.size = property.getSliderTickSize() * DifficultyUtil.stdCircleSizeScale(beatmap.getDifficulty().getCircleSize());
    }

    public double getTickHitTime() {
        return sliderBody.getStartTime() + repeat * sliderBody.getSpanDuration() + getTimeOffset();
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public DirectStdSliderBody getSliderBody() {
        return sliderBody;
    }

    public void setSliderBody(DirectStdSliderBody sliderBody) {
        this.sliderBody = sliderBody;
    }

    public double getPositionProgress() {
        return positionProgress;
    }

    public void setPositionProgress(double positionProgress) {
        this.positionProgress = positionProgress;
    }

    public Vec2 getPositionOnSlider() {
        return sliderBody.getPath().getMeasurer().atLength((float) (sliderBody.getLength() * positionProgress));
    }

    public double getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(double timeOffset) {
        this.timeOffset = timeOffset;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

}
