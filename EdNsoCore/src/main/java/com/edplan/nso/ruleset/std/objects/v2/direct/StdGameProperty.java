package com.edplan.nso.ruleset.std.objects.v2.direct;

public class StdGameProperty {

    private double baseSize = 64;

    private double sliderTickBaseSize = 16;

    private double baseSizeScale = 1;

    public double getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(double baseSize) {
        this.baseSize = baseSize;
    }

    public double getBaseSizeScale() {
        return baseSizeScale;
    }

    public void setBaseSizeScale(double baseSizeScale) {
        this.baseSizeScale = baseSizeScale;
    }

    public double getSize() {
        return baseSize * baseSizeScale;
    }

    public double getSliderTickBaseSize() {
        return sliderTickBaseSize;
    }

    public void setSliderTickBaseSize(double sliderTickBaseSize) {
        this.sliderTickBaseSize = sliderTickBaseSize;
    }

    public double getSliderTickSize() {
        return sliderTickBaseSize * baseSizeScale;
    }
}
