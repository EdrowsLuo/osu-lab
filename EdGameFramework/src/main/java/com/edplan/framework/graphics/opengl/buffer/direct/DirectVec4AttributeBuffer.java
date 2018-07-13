package com.edplan.framework.graphics.opengl.buffer.direct;

import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.math.Vec4;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class DirectVec4AttributeBuffer implements DirectAttributeBuffer {
    public float[] ary;
    public FloatBuffer buffer;
    public Vec4Pointer[] pointers;

    public VertexAttrib attributePointer;

    public DirectVec4AttributeBuffer(int size, VertexAttrib att) {
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
            ary = new float[size * Vec4.FLOATS];
            buffer = BufferUtil.createFloatBuffer(size * Vec4.FLOATS);
            pointers = new Vec4Pointer[size];
            for (int i = 0; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        } else if (pointers.length < size) {
            ary = Arrays.copyOf(ary, size * Vec4.FLOATS);
            buffer = BufferUtil.createFloatBuffer(size * Vec4.FLOATS);
            int i = pointers.length;
            pointers = Arrays.copyOf(pointers, size);
            for (; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        }
    }

    public class ThisPointer extends Vec4Pointer {
        private final int r, g, b, a;

        public ThisPointer(int idx) {
            int offset = idx * Vec4.FLOATS;
            r = offset++;
            g = offset++;
            b = offset++;
            a = offset++;
        }

        @Override
        public void set(float rv, float gv, float bv, float av) {

            ary[r] = rv;
            ary[g] = gv;
            ary[b] = bv;
            ary[a] = av;
        }
    }
}
