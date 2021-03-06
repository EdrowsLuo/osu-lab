package com.edlplan.framework.math;

public class FMath {
    public static final double DPi2 = Math.PI * 2;

    public static final float Pi = (float) Math.PI;

    public static final float Pi2 = (float) (Math.PI * 2);

    public static final float SQRT2 = (float) Math.sqrt(2);

    public static final float PiHalf = (float) (Math.PI * 0.5);

    public static final float SIN30 = (float) Math.sin(Math.PI / 6);

    public static final float SIN60 = (float) Math.sin(Math.PI / 3);

    public static final float[] SIN;
    public static final float[] COS;
    public static final int SC_SIZE = 3600;
    public static final double Delta_Angle;

    static {
        SIN = new float[SC_SIZE];
        COS = new float[SC_SIZE];

        Delta_Angle = Math.PI * 2 / SC_SIZE;
        for (int i = 0; i < SC_SIZE; i++) {
            SIN[i] = (float) Math.sin(Delta_Angle * i);
            COS[i] = (float) Math.cos(Delta_Angle * i);
        }
    }

    public static boolean inInterval(float x1, float x2, float t) {
        return t > x1 && t < x2;
    }

    private static int toBufferIndex(double ang) {
        if (ang < 0) {
            return toBufferIndex(DPi2 - (-ang) % DPi2);
        } else {
            ang %= DPi2;
            return (int) (ang / Delta_Angle);
        }
    }

    public static boolean allmostEqual(float f1, float f2, float t) {
        return Math.abs(f1 - f2) <= t;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? (value) : (value < max ? value : max);
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? (value) : (value < max ? value : max);
    }

    public static float linear(float progress, float bottom, float top) {
        return bottom * (1 - progress) + top * progress;
    }

    public static boolean almostEqual(double v1, double v2, double t) {
        return Math.abs(v1 - v2) <= t;
    }

    public static float linearCut(float c, float c1, float c2, float c3, float v1, float v2, float v3) {
        if (c < c1) {
            return v1;
        } else if (c < c2) {
            return linear((c - c1) / (c2 - c1), v1, v2);
        } else if (c < c3) {
            return linear((c - c2) / (c3 - c2), v2, v3);
        } else {
            return v3;
        }
    }

    public static float squareDistance(float x, float y) {
        return x * x + y * y;
    }

    public static double linear(double progress, double bottom, double top) {
        return bottom * (1 - progress) + top * progress;
    }

}
