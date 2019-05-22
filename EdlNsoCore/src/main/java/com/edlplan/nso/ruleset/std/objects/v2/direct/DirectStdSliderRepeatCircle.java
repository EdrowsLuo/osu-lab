package com.edlplan.nso.ruleset.std.objects.v2.direct;

import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;

public class DirectStdSliderRepeatCircle extends DirectStdCircle {

    private int repeatIndex;

    private DirectStdSliderBody sliderBody;

    public DirectStdSliderRepeatCircle(DirectStdSliderBody sliderBody, double hitTime, int repeatIndex, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        super(repeatIndex % 2 == 0 ? sliderBody.getStartPosition() : sliderBody.getEndPosition(), hitTime, comboIndex, beatmap, property);
        this.repeatIndex = repeatIndex;
        this.sliderBody = sliderBody;
    }

    public int getRepeatIndex() {
        return repeatIndex;
    }

    public DirectStdSliderBody getSliderBody() {
        return sliderBody;
    }
}
