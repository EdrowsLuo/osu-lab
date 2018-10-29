package com.edplan.framework.media.video.tbv.encode;

import java.io.IOException;
import java.io.OutputStream;

public class BufferedByteOutputStream extends OutputStream {
    public static final int MAX_BUFFER = 1024 * 1024 * 5;

    public byte[] ary;
    public int idx;

    public BufferedByteOutputStream(int size) {
        ary = new byte[size];
    }

    public void reset() {
        idx = 0;
    }

    public void expandBuffer(int len) throws IOException {
        if (ary.length == MAX_BUFFER && len > MAX_BUFFER) {
            throw new IOException("big buffer: " + ary.length);
        }
        if (len > MAX_BUFFER) len = MAX_BUFFER;
        byte[] nary = new byte[len];
        for (int i = 0; i < ary.length; i++) {
            nary[i] = ary[i];
        }
        ary = nary;
    }


    @Override
    public void write(int p1) throws IOException {

        if (idx >= ary.length) {
            expandBuffer(ary.length * 2);
            write(p1);
        } else {
            ary[idx] = (byte) p1;
            idx++;
        }
    }

    public int size() {
        return idx;
    }
}
