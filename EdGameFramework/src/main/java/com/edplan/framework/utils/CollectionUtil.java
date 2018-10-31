package com.edplan.framework.utils;

import com.edplan.framework.interfaces.InvokeGetter;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtil {
    public static <T> double getMaxDouble(Collection<T> coll, InvokeGetter<T, Double> getter) {
        double max = Double.MIN_VALUE;
        for (T t : coll) {
            max = Math.max(max, getter.invoke(t));
        }
        return max;
    }

    public static <T> boolean allMatch(Collection<? extends T> collection, JudgeStatement<T> judge) {
        for (T t : collection) {
            if (!judge.judge(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean oneMatch(Collection<? extends T> collection, JudgeStatement<T> judge) {
        for (T t : collection) {
            if (judge.judge(t)) {
                return true;
            }
        }
        return false;
    }
}
