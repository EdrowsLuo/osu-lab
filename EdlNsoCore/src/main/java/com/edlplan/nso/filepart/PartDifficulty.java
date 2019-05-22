package com.edlplan.nso.filepart;

import com.edlplan.framework.utils.dataobject.DataMapObject;
import com.edlplan.framework.utils.dataobject.ItemInfo;
import com.edlplan.framework.utils.dataobject.Struct;
import com.edlplan.framework.utils.U;

public class PartDifficulty extends DataMapObject implements OsuFilePart {
    public static final String HPDrainRate = "HPDrainRate";
    public static final String CircleSize = "CircleSize";
    public static final String OverallDifficulty = "OverallDifficulty";
    public static final String ApproachRate = "ApproachRate";
    public static final String SliderMultiplier = "SliderMultiplier";
    public static final String SliderTickRate = "SliderTickRate";

    public static final String TAG = "Difficulty";

    @ItemInfo private float hPDrainRate;
    @ItemInfo private float circleSize;
    @ItemInfo private float overallDifficulty;
    @ItemInfo private float approachRate;
    @ItemInfo private double sliderMultiplier;
    @ItemInfo private double sliderTickRate;

    @Override
    protected void onLoadStruct(Struct struct) {
        loadStructAnnotation(struct);
    }

    public void setHPDrainRate(float hPDrainRate) {
        this.hPDrainRate = hPDrainRate;
    }

    public float getHPDrainRate() {
        return hPDrainRate;
    }

    public void setCircleSize(float circleSize) {
        this.circleSize = circleSize;
    }

    public float getCircleSize() {
        return circleSize;
    }

    public void setOverallDifficulty(float overallDifficulty) {
        this.overallDifficulty = overallDifficulty;
    }

    public float getOverallDifficulty() {
        return overallDifficulty;
    }

    public void setApproachRate(float approachRate) {
        this.approachRate = approachRate;
    }

    public float getApproachRate() {
        return approachRate;
    }

    public void setSliderMultiplier(double sliderMultiplier) {
        this.sliderMultiplier = sliderMultiplier;
    }

    public double getSliderMultiplier() {
        return sliderMultiplier;
    }

    public void setSliderTickRate(double sliderTickRate) {
        this.sliderTickRate = sliderTickRate;
    }

    public double getSliderTickRate() {
        return sliderTickRate;
    }

    @Override
    public String getTag() {

        return TAG;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        U.appendProperty(sb, PartDifficulty.HPDrainRate, getHPDrainRate()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartDifficulty.CircleSize, getCircleSize()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartDifficulty.OverallDifficulty, getOverallDifficulty()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartDifficulty.ApproachRate, getApproachRate()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartDifficulty.SliderMultiplier, getSliderMultiplier()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartDifficulty.SliderTickRate, getSliderTickRate()).append(U.NEXT_LINE);
        return sb.toString();
    }
}
