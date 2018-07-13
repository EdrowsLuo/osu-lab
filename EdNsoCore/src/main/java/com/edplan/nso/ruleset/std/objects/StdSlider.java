package com.edplan.nso.ruleset.std.objects;

import com.edplan.superutils.math.Vct2;

import java.util.List;

public class StdSlider extends StdHitObject {
    private int sliderIndex;
    private double pixelLength;
    private int repeat;
    private StdPath path;
    private int[] edgeHitsounds;
    private List<Vct2<Integer, Integer>> edgeAdditions;

    public void setSliderIndex(int sliderIndex) {
        this.sliderIndex = sliderIndex;
    }

    public int getSliderIndex() {
        return sliderIndex;
    }

    public void setEdgeAdditions(List<Vct2<Integer, Integer>> edgeAdditions) {
        this.edgeAdditions = edgeAdditions;
    }

    public List<Vct2<Integer, Integer>> getEdgeAdditions() {
        return edgeAdditions;
    }

    public void setEdgeHitsounds(int[] edgeHitsounds) {
        this.edgeHitsounds = edgeHitsounds;
    }

    public int[] getEdgeHitsounds() {
        return edgeHitsounds;
    }

    public void setPixelLength(double pixelLength) {
        this.pixelLength = pixelLength;
    }

    public double getPixelLength() {
        return pixelLength;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setPath(StdPath path) {
        this.path = path;
    }

    public StdPath getPath() {
        return path;
    }

    @Override
    public StdHitObjectType getResType() {

        return StdHitObjectType.Slider;
    }


}
