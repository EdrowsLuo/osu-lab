package com.edplan.nso.beatmapComponent;

import com.edplan.superutils.interfaces.StringMakeable;

import java.util.ArrayList;

import com.edplan.superutils.classes.strings.StringSpliter;

public class Tags implements StringMakeable {
    private ArrayList<String> tags;

    public Tags() {
        tags = new ArrayList<String>();
    }

    public void addTag(String s) {
        tags.add(s);
    }

    @Override
    public String makeString() {

        StringBuilder sb = new StringBuilder();
        for (String i : tags) {
            sb.append(i).append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Tags parse(String l) {
        Tags t = new Tags();
        StringSpliter sp = new StringSpliter(l, ",");
        while (sp.hasNext()) {
            t.addTag(sp.next());
        }
        return t;
    }
}
