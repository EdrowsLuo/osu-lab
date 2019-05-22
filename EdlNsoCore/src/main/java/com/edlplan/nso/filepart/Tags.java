package com.edlplan.nso.filepart;

import java.util.ArrayList;

import com.edlplan.framework.utils.advance.StringSplitter;

public class Tags {
    private ArrayList<String> tags;

    public Tags() {
        tags = new ArrayList<String>();
    }

    public void addTag(String s) {
        tags.add(s);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (String i : tags) {
            sb.append(i).append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Tags parse(String l) {
        Tags t = new Tags();
        StringSplitter sp = new StringSplitter(l, ",");
        while (sp.hasNext()) {
            t.addTag(sp.next());
        }
        return t;
    }
}
