package com.edplan.framework.graphics.opengl.batch.v2.mesh;

import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.v2.AbstractBatch;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;

import java.nio.FloatBuffer;

public class ColorMeshBatch extends AbstractMeshBatch<ColorMesh> {

    private static ColorMeshBatch defaultBatch;

    public static ColorMeshBatch getDefaultBatch() {
        if (defaultBatch == null) {
            defaultBatch = new ColorMeshBatch(1024);
        }
        return defaultBatch;
    }

    private ColorShader shader = ColorShader.DEFAULT.get();

    private FloatBuffer colorBuffer;

    private FloatBuffer positionBuffer;

    public ColorMeshBatch(int bufferVertexCount) {
        super(bufferVertexCount);
        colorBuffer = BufferUtil.createFloatBuffer(4 * bufferVertexCount);
        positionBuffer = BufferUtil.createFloatBuffer(3 * bufferVertexCount);
    }

    @Override
    protected void appendData(ColorMesh colorMesh, int offset, int size) {
        colorBuffer.put(colorMesh.colors.data, colorMesh.colors.offset + offset * 4, size * 4);
        positionBuffer.put(colorMesh.positions.data, colorMesh.positions.offset + offset * 3, size * 3);
    }

    @Override
    protected void clearBuffer() {
        colorBuffer.position(0);
        colorBuffer.limit(maxBatch * 4);
        positionBuffer.position(0);
        positionBuffer.limit(maxBatch * 3);
    }

    @Override
    protected void onApplyToGL() {
        shader.useThis();
        shader.loadShaderGlobals(BatchEngine.getShaderGlobals());

        colorBuffer.position(0);
        colorBuffer.limit(currentSize * 4);
        positionBuffer.position(0);
        positionBuffer.limit(currentSize * 3);

        shader.loadColor(colorBuffer);
        shader.loadPosition(positionBuffer);
        GLWrapped.drawArrays(GLWrapped.GL_TRIANGLES, 0, currentSize);
    }
}
