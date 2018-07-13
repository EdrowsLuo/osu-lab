package com.edplan.framework.graphics.opengl.buffer.direct;

import java.nio.FloatBuffer;

import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;

import java.util.Arrays;

public class DirectVec2AttributeBuffer implements DirectAttributeBuffer {
    public float[] ary;
    public FloatBuffer buffer;
    public Vec2Pointer[] pointers;

    public VertexAttrib attributePointer;

    public DirectVec2AttributeBuffer(int size, VertexAttrib att) {
        ensureSize(size);
        attributePointer = att;
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
            ary = new float[size * Vec2.FLOATS];
            buffer = BufferUtil.createFloatBuffer(size * Vec2.FLOATS);
            pointers = new Vec2Pointer[size];
            for (int i = 0; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        } else if (pointers.length < size) {
            ary = Arrays.copyOf(ary, size * Vec2.FLOATS);
            buffer = BufferUtil.createFloatBuffer(size * Vec2.FLOATS);
            int i = pointers.length;
            pointers = Arrays.copyOf(pointers, size);
            for (; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        }
    }

    public class ThisPointer extends Vec2Pointer {
        private final int x, y;

        public ThisPointer(int idx) {
            int offset = idx * Vec2.FLOATS;
            x = offset++;
            y = offset;
        }

        @Override
        public void set(float xv, float yv) {

            ary[x] = xv;
            ary[y] = yv;
        }
    }
}
