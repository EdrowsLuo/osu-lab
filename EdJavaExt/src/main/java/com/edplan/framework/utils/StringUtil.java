package com.edplan.framework.utils;

import java.util.Iterator;

public class StringUtil {
    public static final String LINE_BREAK = "\n";

    public static String link(String linker, String... ss) {
        if (ss.length == 0) return "";
        StringBuilder sb = new StringBuilder(ss[0]);
        for (int i = 1; i < ss.length; i++) {
            sb.append(linker).append(ss[i]);
        }
        return sb.toString();
    }

    public static String link(String linker, Iterator<String> ss) {
        if (!ss.hasNext()) return "";
        StringBuilder sb = new StringBuilder(ss.next());
        while (ss.hasNext()) {
            sb.append(linker).append(ss.next());
        }
        return sb.toString();
    }

    public static boolean isCommentLine(String string) {
        return string.trim().startsWith("//");
    }
}
