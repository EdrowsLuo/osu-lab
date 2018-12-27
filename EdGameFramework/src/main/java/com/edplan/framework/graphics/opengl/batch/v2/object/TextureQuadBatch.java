package com.edplan.framework.graphics.opengl.batch.v2.object;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.v2.AbstractBatch;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.batch.v2.mesh.ColorMeshBatch;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.shader.advance.Texture2DShader;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextureQuadBatch extends AbstractBatch<TextureQuad> {

    private static final int SIZE_PER_QUAD = 4 * 5;

    private static TextureQuadBatch defaultBatch;

    public static TextureQuadBatch getDefaultBatch() {
        if (defaultBatch == null) {
            defaultBatch = new TextureQuadBatch(1023);
        }
        return defaultBatch;
    }

    private final int maxArySize;

    private FloatBuffer buffer;

    private ShortBuffer indicesBuffer;

    private float[] ary;

    private int offset;

    private GLTexture bindTexture;

    private Texture2DShader shader = Texture2DShader.DEFAULT.get();

    private TextureQuadBatch(int size) {
        if (size > Short.MAX_VALUE / 4 - 10) {
            throw new IllegalArgumentException("过大的QuadBatch");
        }
        maxArySize = size * SIZE_PER_QUAD;
        ary = new float[maxArySize];
        buffer = BufferUtil.createFloatBuffer(maxArySize);
        indicesBuffer = BufferUtil.createShortBuffer(size * 6);
        short[] list = new short[size * 6];
        final int l = list.length;
        short j = 0;
        for (int i = 0; i < l; i += 6) {
            list[i] = j++;
            list[i + 1] = list[i + 3] = j++;
            list[i + 2] = list[i + 5] = j++;
            list[i + 4] = j++;
        }
        indicesBuffer.put(list);
        indicesBuffer.position(0);
    }


    @Override
    protected void onBind() {

    }

    @Override
    protected void onUnbind() {

    }

    @Override
    public void add(TextureQuad textureQuad) {

        if (textureQuad.texture == null) {
            return;
        }

        if (!isBind()) {
            bind();
        }

        if (textureQuad.texture.getTexture() != bindTexture) {
            flush();
            bindTexture = textureQuad.texture.getTexture();
        }

        textureQuad.write(ary, offset);
        offset += SIZE_PER_QUAD;

        if (offset == maxArySize) {
            flush();
        }
    }

    @Override
    protected void clearData() {
        offset = 0;
        buffer.position(0);
        buffer.limit(maxArySize);
    }

    @Override
    protected boolean applyToGL() {
        if (offset != 0) {
            shader.useThis();
            shader.loadShaderGlobals(BatchEngine.getShaderGlobals());
            shader.loadTexture(bindTexture);

            buffer.position(0);
            buffer.put(ary, 0, offset);
            buffer.position(0).limit(offset);

            shader.loadBuffer(buffer);

            GLWrapped.drawElements(
                    GLWrapped.GL_TRIANGLES,
                    offset / SIZE_PER_QUAD * 6,
                    GLES20.GL_UNSIGNED_SHORT,
                    indicesBuffer);
            return true;
        } else {
            return false;
        }
    }

}
