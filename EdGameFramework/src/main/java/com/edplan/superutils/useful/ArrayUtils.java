package com.edplan.superutils.useful;

public class ArrayUtils {
    public static void copy(Object[] t, int ts, Object[] res, int rs, int count) {
        for (int i = 0; i < count; i++) {
            t[i + ts] = res[i + rs];
        }
    }

    public static void copy(Object[] t, Object[] res, int count) {
        copy(t, 0, res, 0, count);
    }


}
