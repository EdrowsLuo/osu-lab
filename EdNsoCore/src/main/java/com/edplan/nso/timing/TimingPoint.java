package com.edplan.nso.timing;

import com.edplan.framework.utils.U;
import com.edplan.nso.beatmapComponent.SampleSet;
import com.edplan.nso.ruleset.std.playing.controlpoint.ControlPoint;

public class TimingPoint extends ControlPoint {

    /**
     * 在lazer原代码里这里是一个enum TimeSignatures,
     * 定义了一拍里有几小节（大概是这么叫的。。。滚去学乐理了）
     */
    private int meter;

    /**
     * 定义音效类型，赞时放置，之后应该用enum代替
     */
    private int sampleType;

    private SampleSet sampleSet = SampleSet.None;

    private int volume = 100;

    /**
     * 在lazer里叫timingChange，一般mapper以Editor里的线的颜色区分
     */
    private boolean inherited = true;

    private boolean kiaiMode = false;

    private boolean omitFirstBarSignature = false;

    private double beatLength;

    private double speedMultiplier;

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setBeatLength(double beatLength) {
        this.beatLength = beatLength;
        speedMultiplier = (beatLength < 0 ? (100.0 / -beatLength) : 1);
    }

    public double getBeatLength() {
        return beatLength;
    }

    public void setOmitFirstBarSignature(boolean omitFirstBarSignature) {
        this.omitFirstBarSignature = omitFirstBarSignature;
    }

    public boolean isOmitFirstBarSignature() {
        return omitFirstBarSignature;
    }

    public void setSampleSet(SampleSet sampleSet) {
        this.sampleSet = sampleSet;
    }

    public SampleSet getSampleSet() {
        return sampleSet;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public int getMeter() {
        return meter;
    }

    public void setSampleType(int sampleType) {
        this.sampleType = sampleType;
    }

    public int getSampleType() {
        return sampleType;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setKiaiMode(boolean kiaiMode) {
        this.kiaiMode = kiaiMode;
    }

    public boolean isKiaiMode() {
        return kiaiMode;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getTime()).append(",");
        sb.append(getBeatLength()).append(",");
        sb.append(getMeter()).append(",");
        sb.append(getSampleType()).append(",");
        sb.append(getSampleSet()).append(",");
        sb.append(U.toVString(isInherited())).append(",");
        sb.append(((isKiaiMode()) ? 1 : 0) + ((isOmitFirstBarSignature()) ? 8 : 0));
        return sb.toString();
    }
}
