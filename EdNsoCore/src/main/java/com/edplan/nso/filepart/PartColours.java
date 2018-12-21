package com.edplan.nso.filepart;

import com.edplan.nso.OsuFilePart;

import android.graphics.Color;

import com.edplan.framework.graphics.opengl.objs.Color4;

import java.util.regex.Pattern;

public class PartColours implements OsuFilePart {
    public static final String TAG = "Colours";

    public static Pattern COLOUR_HEAD = Pattern.compile("Combo(\\d+)");

    public static String SliderBorder = "SliderBorder";

    public static String SliderTrackOverride = "SliderTrackOverride";

    private Colours colours;

    public PartColours() {
        colours = new Colours();
    }

    public void addColour(int index, int c) {
        colours.addColour(index, c);
    }

    public void addColour(int index, int r, int g, int b) {
        colours.addColour(index, Color.argb(255, r, g, b));
    }

    public void setSliderBorder(int r, int g, int b) {
        colours.setSliderBorder(Color4.rgb255(r, g, b));
    }

    public void setSliderTrackOverride(int r, int g, int b) {
        colours.setSliderTrackOverride(Color4.rgb255(r, g, b));
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return (colours != null) ? colours.toString() : "{@Colours}";
    }

}
