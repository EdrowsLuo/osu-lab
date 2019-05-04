package com.edplan.framework.utils;

import java.util.IllegalFormatException;

public class CharArray {
    public char[] ary;
    public int offset;
    public int end;

    public CharArray(char[] ary, int offset, int end) {
        this.ary = ary;
        this.offset = offset;
        this.end = end == -1 ? ary.length : end;
    }

    public CharArray(CharArray copy) {
        this.ary = copy.ary;
        this.offset = copy.offset;
        this.end = copy.end;
    }

    public CharArray(String s) {
        this(s.toCharArray(), 0, -1);
    }

    public char get(int i) {
        return ary[offset + i];
    }

    public void trimBegin() {
        while (offset < end && (ary[offset] == ' ' || ary[offset] == '\n' || ary[offset] == 'r')) {
            offset++;
        }
    }

    public boolean empty() {
        return offset >= end;
    }

    public void nextChar(char target) {
        if (empty() || get(0) != target) {
            throw new FormatNotMatchException(target + " not found at " + offset);
        }
        offset++;
    }

    @Override
    public String toString() {
        return new String(ary, offset, end - offset);
    }

    public static class FormatNotMatchException extends RuntimeException {
        public FormatNotMatchException(String msg) {
            super(msg);
        }
    }

}