package com.edplan.nso.ruleset.std.objects.v2.direct;

import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;

public class DirectStdSliderStartCircle extends DirectStdCircle {

    public DirectStdSliderStartCircle(
            DirectStdSliderBody sliderBody) {
        super(sliderBody.getStartPosition(),
                sliderBody.getStartTime(), sliderBody.getComboIndex(), sliderBody.getBeatmap(), sliderBody.getProperty());
    }

}
