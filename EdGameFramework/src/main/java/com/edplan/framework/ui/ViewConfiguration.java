package com.edplan.framework.ui;

import android.util.Log;
import android.util.TypedValue;

import com.edplan.framework.MContext;

public class ViewConfiguration {
    public static int LONGCLICK_DELAY_MS = 1200;

    public static float START_SCROLL_OFFSET = 10;

    private static float UI_UNIT = 1;

    private static float UI_SCALE = 1;

    public static float DEFAULT_TRANSITION_TIME = 100;

    public static void loadContext(MContext context) {
        UI_UNIT = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getNativeContext().getResources().getDisplayMetrics());
        START_SCROLL_OFFSET = UI_UNIT * 5;
        Log.v("ViewConfiguration", "set UI_UNIT=" + UI_UNIT);
    }

    public static float dp(float dp) {
        return UI_SCALE * UI_UNIT * dp;
    }
}
