package com.edplan.nso.beatmapComponent;

import com.edplan.superutils.interfaces.StringMakeable;

import java.util.ArrayList;

import com.edplan.superutils.classes.strings.StringSpliter;
import com.edplan.superutils.U;

public class Bookmarks implements StringMakeable {
    private ArrayList<Integer> bookmarks;

    public Bookmarks() {
        bookmarks = new ArrayList<Integer>();
    }

    public void addBoommark(int i) {
        bookmarks.add(i);
    }

    @Override
    public String makeString() {

        StringBuilder sb = new StringBuilder();
        for (Integer i : bookmarks) {
            sb.append(i).append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Bookmarks parse(String l) {
        Bookmarks b = new Bookmarks();
        StringSpliter sp = new StringSpliter(l, ",");
        while (sp.hasNext()) {
            b.addBoommark(U.toInt(sp.next()));
        }
        return b;
    }
}
