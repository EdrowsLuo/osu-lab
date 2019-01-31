package com.edplan.framework.graphics.opengl.batch.v2.object;

import android.graphics.Color;

import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.shader.ShaderGlobals;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class PackedColorTriangles {

    float[] ary;

    int offset;

    boolean dirty = true;

    FloatBuffer buffer;

    public PackedColorTriangles() {
        this(4);
    }

    public PackedColorTriangles(int size) {
        ary = new float[ColorTriangle.FloatCount * size];
    }

    public void add(ColorTriangle triangle) {
        while (offset + ColorTriangle.FloatCount >= ary.length) {
            ary = Arrays.copyOf(ary, (int) (ary.length * 1.5));
        }
        dirty = true;
        triangle.write(ary, offset);
        offset += ColorTriangle.FloatCount;
    }

    public void reset() {
        offset = 0;
        dirty = true;
    }

    private void prepareBuffer() {
        if (buffer == null || buffer.capacity() < offset) {
            buffer = BufferUtil.createFloatBuffer(offset);
            dirty = true;
        }
        if (dirty) {
            buffer.position(0);
            buffer.put(ary, 0, offset);
            buffer.position(0).limit(offset);
        }
    }

    public void render(ColorShader shader, ShaderGlobals globals) {

        int vs = offset / ColorTriangle.FloatCountPerVertex;
        if (vs < 3) {
            return;
        }

        BatchEngine.flush();
        prepareBuffer();

        shader.useThis();
        shader.loadShaderGlobals(globals);
        shader.loadBuffer(buffer);

        GLWrapped.drawArrays(GLWrapped.GL_TRIANGLES, 0, vs);
    }

    public void ignoreBufferLoadRender(Texture3DShader shader, GLTexture texture, ShaderGlobals globals) {

        int vs = offset / ColorTriangle.FloatCountPerVertex;
        if (vs < 3) {
            return;
        }

        BatchEngine.flush();
        prepareBuffer();

        shader.useThis();
        shader.loadShaderGlobals(globals);
        shader.loadTexture(texture);

        GLWrapped.drawArrays(GLWrapped.GL_TRIANGLES, 0, vs);
    }


}
