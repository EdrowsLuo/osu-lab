package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Function;

public class ArrayUtils {
    public static void copy(Object[] t, int ts, Object[] res, int rs, int count) {
        for (int i = 0; i < count; i++) {
            t[i + ts] = res[i + rs];
        }
    }

    public static void copy(Object[] t, Object[] res, int count) {
        copy(t, 0, res, 0, count);
    }

    public static <T, K> K[] reflect(T[] t, Function<T,K> function,K[] k) {
        for (int i = 0; i < t.length; i++) {
            k[i] = function.reflect(t[i]);
        }
        return k;
    }

}
