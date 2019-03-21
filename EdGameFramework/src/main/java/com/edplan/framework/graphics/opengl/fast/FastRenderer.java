package com.edplan.framework.graphics.opengl.fast;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.math.its.IColor4;
import com.edplan.framework.math.its.IVec2;
import com.edplan.framework.math.its.IVec3;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class FastRenderer {
    public static final int MAX_VERTEX_COUNT = 4000;

    public static final int MAX_INDICES_COUNT = 10000;

    private FastShader shader;

    private float[] positionArray;

    private float[] textureCoordArray;

    private float[] colorArray;

    private FloatBuffer positionBuffer, textureCoordBuffer, colorBuffer;

    private FastVertex[] vertexs;

    private int idx;

    private short[] indices;

    private ShortBuffer indicesBuffer;

    private int idcx;

    private GLTexture texture;

    private BlendType blendType;

    private boolean isRendering = false;

    private BaseCanvas frameCanvas;

    public FastRenderer() {
        shader = new FastShader();
        ensureSize(32, 32 * 3);
    }

    /**
     * 用来确保可以放置的顶点数和索引数，
     * 原来的数据可能被刷新！所以请在start()前就确保绘制的数量
     */
    public void ensureSize(int vertexCount, int indiceCount) {
        vertexCount = Math.min(MAX_VERTEX_COUNT, vertexCount);
        indiceCount = (indiceCount / 3 + 1) * 3;
        indiceCount = Math.min(MAX_INDICES_COUNT, indiceCount);
        if (vertexs == null || vertexs.length < vertexCount) {
            if (vertexs != null) {
                final int preLength = vertexs.length;
                vertexs = Arrays.copyOf(vertexs, vertexCount);
                for (int i = preLength; i < vertexs.length; i++) {
                    vertexs[i] = new FastVertex(i);
                }
            } else {
                vertexs = new FastVertex[vertexCount];
                for (int i = 0; i < vertexs.length; i++) {
                    vertexs[i] = new FastVertex(i);
                }
            }
            positionArray = new float[Vec3.FLOATS * vertexCount];
            textureCoordArray = new float[Vec2.FLOATS * vertexCount];
            colorArray = new float[Color4.FLOATS * vertexCount];

            positionBuffer = BufferUtil.createFloatBuffer(positionArray.length);
            textureCoordBuffer = BufferUtil.createFloatBuffer(textureCoordArray.length);
            colorBuffer = BufferUtil.createFloatBuffer(colorArray.length);
        }
        if (indices == null || indices.length < indiceCount) {
            indices = new short[indiceCount];
            indicesBuffer = BufferUtil.createShortBuffer(indices.length);
        }
    }

    public void addIndices(short... ind) {
        if (idcx + ind.length >= indices.length)
            flush();
        for (int i = 0; i < ind.length; i++) {
            indices[idcx] = ind[i];
            idcx++;
        }
    }

    public void getNextVertexs(FastVertex[] ary) {
        if (idx + ary.length > vertexs.length) {
            flush();
        }
        for (int i = 0; i < ary.length; i++) {
            ary[i] = vertexs[idx++];
        }
    }

    public void setCurrentTexture(AbstractTexture t) {
        if (t.getTexture() != texture) {
            flush();
            texture = t.getTexture();
        }
    }

    public void setBlendType(BlendType blendType) {
        if (this.blendType != blendType) {
            flush();
            this.blendType = blendType;
            frameCanvas.getBlendSetting().setBlendType(blendType);
        }
    }

    public BlendType getBlendType() {
        return blendType;
    }

    public void resetIdxData() {
        idx = 0;
        idcx = 0;
        positionBuffer.clear();
        textureCoordBuffer.clear();
        colorBuffer.clear();
        indicesBuffer.clear();
    }

    public void start(BaseCanvas canvas) {
        if (isRendering)
            throw new RuntimeException("A FastRenderer can't call start when rendering");
        isRendering = true;
        frameCanvas = canvas;
        blendType = frameCanvas.getBlendSetting().getBlendType();
        resetIdxData();
    }


    public void flush() {
        if (texture == null || idcx == 0) return;
        shader.useThis();
        shader.bindTexture(texture);
        shader.loadCamera(frameCanvas.getCamera());

        positionBuffer.position(0);
        positionBuffer.put(positionArray, 0, idx * Vec3.FLOATS);
        positionBuffer.position(0);

        textureCoordBuffer.position(0);
        textureCoordBuffer.put(textureCoordArray, 0, idx * Vec2.FLOATS);
        textureCoordBuffer.position(0);

        colorBuffer.position(0);
        colorBuffer.put(colorArray, 0, idx * Color4.FLOATS);
        colorBuffer.position(0);

        indicesBuffer.position(0);
        indicesBuffer.put(indices, 0, idcx);
        indicesBuffer.position(0);

        shader.loadAttibutes(positionBuffer, textureCoordBuffer, colorBuffer);

        //vao.putData(dataBuffer,idx*Float.BYTES);
        //shader.bindAttributes();
        //shader.bindAttributes(dataBuffer);
        //GLWrapped.drawArrays(GLWrapped.GL_TRIANGLES,0,idx);
        GLWrapped.drawElements(GLWrapped.GL_TRIANGLES, idcx, GLES20.GL_UNSIGNED_SHORT, indicesBuffer);
        resetIdxData();
    }

    public void end() {
        if (!isRendering)
            throw new RuntimeException("A FastRenderer can't call start when rendering");
        flush();
        isRendering = false;
    }

    public class FastVertex {
        public static final int FLOAT_COUNT = 9;

        public final short index;

        //定义了位置坐标
        protected final int x, y, z;

        //定义了材质坐标
        protected final int u, v;

        //定义了颜色
        protected final int r, g, b, a;

        public final PositionPointer Position;

        public final TextureCoordPointer TextureCoord;

        public final ColorPointer Color;

        public FastVertex(int index) {
            this.index = (short) index;
            int offset;

            Position = new PositionPointer();
            TextureCoord = new TextureCoordPointer();
            Color = new ColorPointer();

            offset = index * Vec3.FLOATS;
            this.x = offset++;
            this.y = offset++;
            this.z = offset++;

            offset = index * Vec2.FLOATS;
            this.u = offset++;
            this.v = offset++;

            offset = index * Color4.FLOATS;
            this.r = offset++;
            this.g = offset++;
            this.b = offset++;
            this.a = offset++;
        }

        public class PositionPointer implements IVec3 {

            @Override
            public void set(float xv, float yv, float zv) {
                positionArray[x] = xv;
                positionArray[y] = yv;
                positionArray[z] = zv;
            }

            @Override
            public void set(Vec2 v) {
                set(v.x, v.y);
            }

            @Override
            public void set(float xv, float yv) {
                positionArray[x] = xv;
                positionArray[y] = yv;
            }

            @Override
            public float getX() {
                return positionArray[x];
            }

            @Override
            public float getY() {
                return positionArray[y];
            }

            @Override
            public void setX(float v) {
                positionArray[x] = v;
            }

            @Override
            public void setY(float v) {
                positionArray[y] = v;
            }

            @Override
            public float getZ() {
                return positionArray[z];
            }

            @Override
            public void setZ(float v) {
                positionArray[z] = v;
            }
        }

        public class TextureCoordPointer implements IVec2 {

            @Override
            public void set(Vec2 v) {
                set(v.x, v.y);
            }

            @Override
            public void set(float xv, float yv) {
                textureCoordArray[u] = xv;
                textureCoordArray[v] = yv;
            }

            @Override
            public float getX() {
                return textureCoordArray[u];
            }

            @Override
            public float getY() {
                return textureCoordArray[v];
            }

            @Override
            public void setX(float v) {
                textureCoordArray[u] = v;
            }

            @Override
            public void setY(float vl) {
                textureCoordArray[v] = vl;
            }
        }

        public class ColorPointer implements IColor4 {
            @Override
            public float getRed() {
                return colorArray[r];
            }

            @Override
            public float getGreen() {
                return colorArray[g];
            }

            @Override
            public float getBlue() {
                return colorArray[b];
            }

            @Override
            public float getAlpha() {
                return colorArray[a];
            }

            @Override
            public void set(float rv, float gv, float bv, float av) {
                colorArray[r] = rv;
                colorArray[g] = gv;
                colorArray[b] = bv;
                colorArray[a] = av;
            }
        }
    }
}
