package com.edlplan.framework.math;

public class MathExt {


    public static int floorMod(int v, int m) {
        int r = v - floorDiv(v, m) * m;
        return r;
    }

    public static int floorDiv(int x, int y) {
        int r = x / y;
        if ((x ^ y) < 0 && (r * y != x)) {
            r--;
        }
        return r;
    }
}
