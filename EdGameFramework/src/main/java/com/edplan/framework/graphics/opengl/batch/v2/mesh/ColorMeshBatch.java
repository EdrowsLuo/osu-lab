package com.edplan.framework.graphics.opengl.batch.v2.mesh;

import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.v2.AbstractBatch;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;

import java.nio.FloatBuffer;

public class ColorMeshBatch extends AbstractBatch<ColorMesh> {

    private ColorShader shader = ColorShader.DEFAULT.get();

    private FloatBuffer colorBuffer;

    private FloatBuffer positionBuffer;

    private int maxBatch;

    private int currentSize;

    public ColorMeshBatch(int bufferVertexCount) {
        maxBatch = bufferVertexCount;
        colorBuffer = BufferUtil.createFloatBuffer(4 * bufferVertexCount);
        positionBuffer = BufferUtil.createFloatBuffer(3 * bufferVertexCount);
    }

    @Override
    protected void onBind() {

    }

    @Override
    protected void onUnbind() {

    }

    @Override
    public void add(ColorMesh colorMesh) {
        if (colorMesh.size() > maxBatch) {
            throw new IllegalArgumentException("such a big mesh (qwq)");
        }
        if (currentSize + colorMesh.size() > maxBatch) {
            flush();
        }
        final int size = colorMesh.size();
        colorBuffer.put(colorMesh.colors.data, colorMesh.colors.offset, size * 4);
        positionBuffer.put(colorMesh.positions.data, colorMesh.positions.offset, size * 3);
        currentSize += size;
    }

    @Override
    protected void clearData() {
        currentSize = 0;
        colorBuffer.position(0);
        colorBuffer.limit(maxBatch * 4);
        positionBuffer.position(0);
        colorBuffer.limit(maxBatch * 3);
    }

    @Override
    protected void applyToGL() {
        if (currentSize == 0) {
            return;
        }
        shader.useThis();

        shader.loadAlpha(BatchEngine.getShaderGlobals().alpha);
        shader.loadMatrix(BatchEngine.getShaderGlobals().camera);

        colorBuffer.position(0);
        colorBuffer.limit(currentSize * 4);
        positionBuffer.position(0);
        positionBuffer.limit(currentSize * 3);

        shader.loadColor(colorBuffer);
        shader.loadPosition(positionBuffer);
        GLWrapped.drawArrays(GLWrapped.GL_TRIANGLES, 0, currentSize);
    }

}
