package com.edplan.framework.ui.text.font.bmfont;

public class KerningPair {
    public final char first;
    public final char second;

    public KerningPair(char first, char second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {

        return hashCode(first, second);
    }

    public static int hashCode(char first, char second) {
        return (int) first + ((int) second) * 157;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof KerningPair) {
            KerningPair other = (KerningPair) obj;
            return (this.first == other.first) && (this.second == other.second);
        } else return super.equals(obj);
    }
}
