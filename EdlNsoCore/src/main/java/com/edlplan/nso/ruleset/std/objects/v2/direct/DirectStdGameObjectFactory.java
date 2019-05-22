package com.edlplan.nso.ruleset.std.objects.v2.direct;

import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdCircle;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdSlider;

public class DirectStdGameObjectFactory {

    public DirectStdCircle createCircle(StdCircle circle, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        return new DirectStdCircle(circle, comboIndex, beatmap, property);
    }

    public DirectStdSliderBody createSliderBody(StdSlider slider, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        return new DirectStdSliderBody(slider, comboIndex, beatmap, property);
    }

    public DirectStdSliderStartCircle createSliderStartCircle(DirectStdSliderBody body) {
        return new DirectStdSliderStartCircle(body);
    }

    public DirectStdSliderEndCircle createSliderEndCircle(DirectStdSliderBody body) {
        return new DirectStdSliderEndCircle(body);
    }

    public DirectStdSliderTick createTick(DirectStdSliderBody sliderBody,
                                          double positionProgress,
                                          int repeat,
                                          double timeOffset) {
        return new DirectStdSliderTick(sliderBody, positionProgress, repeat, timeOffset);
    }



}
