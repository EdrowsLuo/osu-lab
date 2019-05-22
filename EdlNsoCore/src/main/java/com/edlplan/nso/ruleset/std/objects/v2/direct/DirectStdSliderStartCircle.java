package com.edlplan.nso.ruleset.std.objects.v2.direct;

public class DirectStdSliderStartCircle extends DirectStdCircle {

    public DirectStdSliderStartCircle(
            DirectStdSliderBody sliderBody) {
        super(sliderBody.getStartPosition(),
                sliderBody.getStartTime(), sliderBody.getComboIndex(), sliderBody.getBeatmap(), sliderBody.getProperty());
    }

}
