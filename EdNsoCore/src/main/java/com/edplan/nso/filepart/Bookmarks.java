package com.edplan.nso.filepart;

import java.util.ArrayList;

import com.edplan.framework.utils.advance.StringSplitter;
import com.edplan.framework.utils.U;

public class Bookmarks {
    private ArrayList<Integer> bookmarks;

    public Bookmarks() {
        bookmarks = new ArrayList<Integer>();
    }

    public void addBoommark(int i) {
        bookmarks.add(i);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (Integer i : bookmarks) {
            sb.append(i).append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Bookmarks parse(String l) {
        Bookmarks b = new Bookmarks();
        StringSplitter sp = new StringSplitter(l, ",");
        while (sp.hasNext()) {
            b.addBoommark(U.toInt(sp.next()));
        }
        return b;
    }
}