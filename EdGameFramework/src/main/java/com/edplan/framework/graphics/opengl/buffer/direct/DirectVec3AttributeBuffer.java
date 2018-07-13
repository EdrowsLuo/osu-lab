package com.edplan.framework.graphics.opengl.buffer.direct;

import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.math.Vec3;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class DirectVec3AttributeBuffer implements DirectAttributeBuffer {
    public float[] ary;
    public FloatBuffer buffer;
    public Vec3Pointer[] pointers;

    public VertexAttrib attributePointer;

    public DirectVec3AttributeBuffer(int size, VertexAttrib att) {
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
            ary = new float[size * Vec3.FLOATS];
            buffer = BufferUtil.createFloatBuffer(size * Vec3.FLOATS);
            pointers = new Vec3Pointer[size];
            for (int i = 0; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        } else if (pointers.length < size) {
            ary = Arrays.copyOf(ary, size * Vec3.FLOATS);
            buffer = BufferUtil.createFloatBuffer(size * Vec3.FLOATS);
            int i = pointers.length;
            pointers = Arrays.copyOf(pointers, size);
            for (; i < pointers.length; i++) {
                pointers[i] = new ThisPointer(i);
            }
        }
    }

    public class ThisPointer extends Vec3Pointer {
        private final int x, y, z;

        public ThisPointer(int idx) {
            int offset = idx * Vec3.FLOATS;
            x = offset++;
            y = offset++;
            z = offset;
        }

        @Override
        public void set(float xv, float yv, float zv) {

            ary[x] = xv;
            ary[y] = yv;
            ary[z] = zv;
        }
    }
}
