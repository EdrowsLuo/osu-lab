package com.edlplan.framework.graphics.opengl.buffer.direct;

import com.edlplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edlplan.framework.graphics.opengl.buffer.BufferUtil;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class DirectFloatAttributeBuffer implements DirectAttributeBuffer {
    public float[] ary;
    public FloatBuffer buffer;
    public FloatPointer[] pointers;

    public VertexAttrib attributePointer;

    public DirectFloatAttributeBuffer(int size, VertexAttrib att) {
        ensureSize(size);
        attributePointer = att;
    }

    public void updateRange(int start, int end) {

    }

    @Override
    public void loadToAttribute() {

        buffer.position(0);
        buffer.put(ary);
        buffer.position(0);
        attributePointer.loadData(buffer);
    }

    @Override
    public void ensureSize(int size) {
        if (ary == null) {
            ary = new float[size];
            buffer = BufferUtil.createFloatBuffer(size);
            pointers = new FloatPointer[size];
            for (int i = 0; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        } else if (pointers.length < size) {
            ary = Arrays.copyOf(ary, size);
            buffer = BufferUtil.createFloatBuffer(size);
            int i = pointers.length;
            pointers = Arrays.copyOf(pointers, size);
            for (; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        }
    }

    public class ThisPointer extends FloatPointer {
        private final int x;

        public ThisPointer(int idx) {
            x = idx;
        }

        @Override
        public void set(float v) {

            ary[x] = v;
        }
    }
}
