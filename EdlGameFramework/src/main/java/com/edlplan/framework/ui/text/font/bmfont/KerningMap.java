package com.edlplan.framework.ui.text.font.bmfont;

import java.util.HashMap;

public class KerningMap {
    private HashMap<Integer, FNTKerning> kerningmap = new HashMap<Integer, FNTKerning>();

    public FNTKerning getKerning(char first, char second) {
        return kerningmap.get(KerningPair.hashCode(first, second));
    }

    public void addKerning(FNTKerning kerning) {
        kerningmap.put(KerningPair.hashCode(kerning.first, kerning.second), kerning);
    }

    public int size() {
        return kerningmap.size();
    }
}
