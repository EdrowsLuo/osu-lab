package com.edplan.framework.test.performance.ui;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.ui.ViewRoot;
import com.edplan.framework.graphics.opengl.GLWrapped;

public class FrameRenderMonitor extends EdView {
    public static float[] timelist = new float[90];

    public static float avg;

    public static Monitor frameRenderTime = new Monitor();

    public static Monitor layout = new Monitor();

    public static Monitor drawUI = new Monitor();

    public static int drawCalls;

    public static int possibleBlockTimes;

    public static int getFPS() {
        return (avg != 0) ? (int) (1000 / avg) : 0;
    }

    public static int getFrameDeltaTimeAvg() {
        return Math.round(avg);
    }

    public FrameRenderMonitor(MContext context) {
        super(context);
    }

    public static void update(MContext c) {
        frameRenderTime.update(Tracker.TotalFrameTime.totalTimeMS);
        layout.update(Tracker.InvalidateMeasureAndLayout.totalTimeMS);
        drawUI.update(Tracker.DrawUI.totalTimeMS);
        drawCalls = GLWrapped.frameDrawCalls();
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

    @Override
    protected void onDraw(BaseCanvas canvas) {

        float deltaTime = (float) getContext().getFrameDeltaTime();
        GLPaint ntp = new GLPaint();
        ntp.setStrokeWidth(1.2f);
        //ntp.setColorMixRate(1);
        ntp.setMixColor(Color4.White);
        canvas.save();
        float lengthScale = 5;
        ntp.setMixColor(Color4.rgba(0, 1, 0, 1));
        canvas.drawLine(10, 50, 10 + 18 * 6, 50, ntp);
        ntp.setMixColor(Color4.rgba(1, 1, 0, 1));
        canvas.drawLine(10, 55, 10 + deltaTime * lengthScale, 55, ntp);
        ntp.setMixColor(Color4.White);
        for (int i = timelist.length - 1; i > 0; i--) {
            timelist[i] = timelist[i - 1];
        }
        timelist[0] = deltaTime;
        for (int i = 1; i < timelist.length; i++) {
            canvas.drawLine(10, 55 + 6 * i, 10 + timelist[i] * lengthScale, 55 + 6 * i, ntp);
        }
        float avg = 0;
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
        ntp.setStrokeWidth(3);
        ntp.setMixColor(Color4.rgba(1, 0, 0, 1));
        canvas.drawLine(10 + avg * lengthScale, 40, 10 + avg * lengthScale, 60 + 6 * timelist.length, ntp);
        //ntp.setStrokeWidth(2);
        //ntp.setMixColor(Color4.rgba(0,1,0,1));
        //canvas.drawLine(10+max*lengthScale,40,10+max*lengthScale,60+6*timelist.length,ntp);
        ntp.setStrokeWidth(2);
        ntp.setMixColor(Color4.rgba(0, 0, 1, 1));
        canvas.drawLine(10 + min * lengthScale, 40, 10 + min * lengthScale, 60 + 6 * timelist.length, ntp);
        ntp.setStrokeWidth(3);
        ntp.setMixColor(Color4.rgba(0.5f, 0.5f, 1, 1));
        canvas.drawLine(10 + avg_nogc * lengthScale, 40, 10 + avg_nogc * lengthScale, 60 + 6 * timelist.length, ntp);
        canvas.restore();
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
