package com.edplan.framework.utils.io;

import com.edplan.framework.utils.annotation.NotThreadSafe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * 在内存里创建一个类似文件行为的对象，为单线程设计的x
 *
 * **************************************************
 * *                                              *
 * *             android又帮你写好代码了x         *
 * *                                              *
 * **************************************************
 * *                                              *
 * *     突然发现android的那个文件大小是固定的x    *
 * *                                              *
 * **************************************************
 *
 */
@NotThreadSafe
public class MemoryFile {

    private static final int EXP_LEVEL_1 = 1024 * 64;

    private static final int EXP_LEVEL_2 = 1024 * 1024;

    private static final int EXP_MAX_SIZE = 1024 * 512;

    private byte[] data;

    private int length;

    private MemoryInputStream inputStream;

    private MemoryOutputStream outputStream;

    public MemoryFile(int basesize) {
        data = new byte[basesize];
    }

    private int calNextSize() {
        if (data.length < EXP_LEVEL_1) {
            return data.length * 2;
        } else if (data.length < EXP_LEVEL_2) {
            return data.length * 3 / 2;
        } else {
            return data.length + EXP_MAX_SIZE;
        }
    }

    private void expendSize(int size) {
        data = Arrays.copyOf(data, size);
    }

    public void expendDefault() {
        expendSize(calNextSize());
    }

    public InputStream getInputStream() {
        if (inputStream == null) {
            inputStream = new MemoryInputStream();
        }
        return inputStream;
    }

    public OutputStream getOutputStream() {
        if (outputStream == null) {
            outputStream = new MemoryOutputStream();
        }
        return outputStream;
    }

    public void writeTo(OutputStream out, int offset, int len) throws IOException {
        out.write(data, offset, Math.min(len, length - offset));
    }

    public void writeTo(OutputStream out) throws IOException {
        writeTo(out, 0, length);
    }

    public int getLength() {
        return length;
    }

    private class MemoryInputStream extends InputStream{

        private int ptr = 0;

        @Override
        public int read() throws IOException {
            if (ptr == length) {
                return -1;
            } else {
                return 0xff & data[ptr++];
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (ptr == length) {
                return -1;
            } else {
                int endPtr = Math.min(ptr + len, length);
                int l = endPtr - ptr;
                System.arraycopy(data, ptr, b, off, l);
                ptr = endPtr;
                return l;
            }
        }

        @Override
        public int available() throws IOException {
            return length - ptr;
        }
    }

    private class MemoryOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            if (data.length == length) {
                expendDefault();
            }
            data[length++] = (byte) b;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            int targetSize = len + length;
            while (data.length <= targetSize) {
                expendDefault();
            }
            System.arraycopy(b, off, data, length, len);
            length = targetSize;
        }
    }

}
