package com.edplan.framework.graphics.opengl.batch.v2.mesh;

import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.shader.advance.LegacyTexture3DShader;

import java.nio.FloatBuffer;

public class TextureMeshBatch extends AbstractMeshBatch<TextureMesh> {

    private static TextureMeshBatch defaultBatch;

    public static TextureMeshBatch getDefaultBatch() {
        if (defaultBatch == null) {
            defaultBatch = new TextureMeshBatch(1023);
        }
        return defaultBatch;
    }

    private LegacyTexture3DShader shader = LegacyTexture3DShader.DEFAULT.get();

    private FloatBuffer colorBuffer;

    private FloatBuffer positionBuffer;

    private FloatBuffer texCoordBuffer;

    public TextureMeshBatch(int maxBatch) {
        super(maxBatch);
        colorBuffer = BufferUtil.createFloatBuffer(4 * maxBatch);
        positionBuffer = BufferUtil.createFloatBuffer(3 * maxBatch);
        texCoordBuffer = BufferUtil.createFloatBuffer(2 * maxBatch);
    }

    private GLTexture texture;

    @Override
    protected boolean checkStateForFlush(TextureMesh mesh, int offset, int size) {
        if (texture != mesh.texture.getTexture()) {
            return true;
        }
        return super.checkStateForFlush(mesh, offset, size);
    }

    @Override
    protected void appendData(TextureMesh mesh, int offset, int size) {
        texture = mesh.texture.getTexture();
        colorBuffer.put(mesh.colors.data, mesh.colors.offset + offset * 4, size * 4);
        positionBuffer.put(mesh.positions.data, mesh.positions.offset + offset * 3, size * 3);
        texCoordBuffer.put(mesh.textureCoord.data, mesh.textureCoord.offset + offset * 2, size * 2);
    }

    @Override
    protected void clearBuffer() {
        colorBuffer.position(0);
        colorBuffer.limit(maxBatch * 4);
        positionBuffer.position(0);
        positionBuffer.limit(maxBatch * 3);
        texCoordBuffer.position(0);
        texCoordBuffer.limit(maxBatch * 2);
    }

    @Override
    protected boolean onApplyToGL() {
        shader.useThis();
        shader.loadShaderGlobals(BatchEngine.getShaderGlobals());

        colorBuffer.position(0);
        colorBuffer.limit(currentSize * 4);
        positionBuffer.position(0);
        positionBuffer.limit(currentSize * 3);
        texCoordBuffer.position(0);
        texCoordBuffer.limit(currentSize * 2);

        shader.loadColor(colorBuffer);
        shader.loadPosition(positionBuffer);
        shader.loadTextureCoord(texCoordBuffer);
        shader.loadTexture(texture);
        GLWrapped.drawArrays(GLWrapped.GL_TRIANGLES, 0, currentSize);
        return true;
    }
}
