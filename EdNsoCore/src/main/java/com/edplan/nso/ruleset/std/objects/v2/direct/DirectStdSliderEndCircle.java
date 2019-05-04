package com.edplan.nso.ruleset.std.objects.v2.direct;

import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;

public class DirectStdSliderEndCircle extends DirectStdCircle {

    private DirectStdSliderBody sliderBody;

    public DirectStdSliderEndCircle(
            DirectStdSliderBody sliderBody) {
        super(sliderBody.getRepeat() % 2 == 1 ? sliderBody.getStartPosition() : sliderBody.getEndPosition(),
                sliderBody.getEndTime(), sliderBody.getComboIndex(), sliderBody.getBeatmap(), sliderBody.getProperty());
    }

}
