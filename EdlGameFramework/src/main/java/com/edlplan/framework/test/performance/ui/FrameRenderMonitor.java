package com.edlplan.framework.test.performance.ui;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.GLWrapped;
import com.edlplan.framework.test.performance.Tracker;

public class FrameRenderMonitor {
    public static float[] timelist = new float[90];

    public static float avg;

    public static Monitor frameRenderTime = new Monitor();

    public static Monitor layout = new Monitor();

    public static Monitor drawUI = new Monitor();

    public static int drawCalls;

    public static int possibleBlockTimes;

    public static int fboCreate;

    public static int getFPS() {
        return (avg != 0) ? Math.round(1000 / avg) : 0;
    }

    public static int getFrameDeltaTimeAvg() {
        return Math.round(avg);
    }

    public static void update(MContext c) {
        frameRenderTime.update(Tracker.TotalFrameTime.totalTimeMS);
        layout.update(Tracker.InvalidateMeasureAndLayout.totalTimeMS);
        drawUI.update(Tracker.DrawUI.totalTimeMS);
        drawCalls = GLWrapped.frameDrawCalls();
        fboCreate = GLWrapped.getFboCreate();
        possibleBlockTimes = 0;
        float deltaTime = (float) c.getFrameDeltaTime();
        for (int i = timelist.length - 1; i > 0; i--) {
            timelist[i] = timelist[i - 1];
        }
        timelist[0] = deltaTime;
        avg = 0;
        float max = 0;
        float min = 99999;
        float avg_nogc = 0;
        int count_nogc = 0;
        for (float t : timelist) {
            avg += t;
            if (t < min) {
                min = t;
            }
            if (t > max) {
                max = t;
            }
        }
        avg_nogc /= count_nogc;
        avg /= timelist.length;
        for (float t : timelist) {
            if (t > avg * 3) {
                //认为这帧发生了阻塞
                possibleBlockTimes++;
            }
        }
    }

    public static class Monitor {
        public float[] timelist = new float[90];

        public float avg;

        public void update(double tt) {
            float deltaTime = (float) tt;
            for (int i = timelist.length - 1; i > 0; i--) {
                timelist[i] = timelist[i - 1];
            }
            timelist[0] = deltaTime;
            avg = 0;
            float max = 0;
            float min = 99999;
            float avg_nogc = 0;
            int count_nogc = 0;
            for (float t : timelist) {
                avg += t;
                if (t < min) {
                    min = t;
                }
                if (t > max) {
                    max = t;
                }
                if (t < 400) {
                    avg_nogc += t;
                    count_nogc++;
                }
            }
            avg_nogc /= count_nogc;
            avg /= timelist.length;
        }
    }
}
