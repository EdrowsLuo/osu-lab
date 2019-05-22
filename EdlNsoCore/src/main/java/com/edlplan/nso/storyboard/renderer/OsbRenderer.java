package com.edlplan.nso.storyboard.renderer;

import android.opengl.GLES20;

import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.GLWrapped;
import com.edlplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.math.its.IVec2;

import java.nio.IntBuffer;
import java.util.Arrays;

import com.edlplan.framework.graphics.opengl.buffer.direct.DirectVec2AttributeBuffer;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.graphics.opengl.buffer.direct.DirectVec4AttributeBuffer;
import com.edlplan.framework.graphics.opengl.buffer.direct.DirectIntAttributeBuffer;
import com.edlplan.framework.graphics.opengl.buffer.direct.DirectAttributeBuffer;

import java.util.Stack;

import com.edlplan.framework.graphics.opengl.buffer.direct.Vec4Pointer;
import com.edlplan.framework.graphics.opengl.buffer.direct.IntPointer;
import com.edlplan.framework.graphics.opengl.buffer.direct.Vec2Pointer;
import com.edlplan.framework.timing.PreciseTimeline;
import com.edlplan.framework.test.performance.Tracker;

public class OsbRenderer {
    public static final Tracker.TrackNode UpdateBuffer;

    static {
        UpdateBuffer = Tracker.register("OsbRenderer::UpdateBuffer");
    }

    public static final int INITIAL_VERTEXS = 32;

    public static final int MAX_INDICES_COUNT = 30000;

    private final OsbShader shader;

    private final DirectVec2AttributeBuffer anchorOffsetBuffer, textureCoordBuffer;

    private final DirectVec4AttributeBuffer positionXBuffer;
    private final DirectIntAttributeBuffer XEasingBuffer;
    private final DirectVec4AttributeBuffer scaleXBuffer;
    private final DirectIntAttributeBuffer XSEasingBuffer;

    private final DirectVec4AttributeBuffer positionYBuffer;
    private final DirectIntAttributeBuffer YEasingBuffer;
    private final DirectVec4AttributeBuffer scaleYBuffer;
    private final DirectIntAttributeBuffer YSEasingBuffer;

    private final DirectVec4AttributeBuffer rotationBuffer;
    private final DirectIntAttributeBuffer REasingBuffer;

    private final DirectVec4AttributeBuffer alphaBuffer;
    private final DirectIntAttributeBuffer AEasingBuffer;

    private final DirectVec4AttributeBuffer color0Buffer, color1Buffer;
    private final DirectVec2AttributeBuffer colorTimeBuffer;
    private final DirectIntAttributeBuffer CEasingBuffer;

    private final DirectAttributeBuffer[] buffers;

    private OsbVertex[] vertexs;

    private final Stack<OsbVertex> vertexStack = new Stack<OsbVertex>();

    private int[] indices;

    private IntBuffer indicesBuffer;

    private int idcx;

    private GLTexture texture;

    private BlendType blendType;

    private boolean isRendering = false;

    private BaseCanvas frameCanvas;

    private final PreciseTimeline timeline;

    public float frameTime;

    public OsbRenderer(PreciseTimeline timeline) {
        this.timeline = timeline;
        shader = new OsbShader();
        anchorOffsetBuffer = new DirectVec2AttributeBuffer(INITIAL_VERTEXS, shader.aAnchorOffset);
        textureCoordBuffer = new DirectVec2AttributeBuffer(INITIAL_VERTEXS, shader.aTextureCoord);
        positionXBuffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aPositionX);
        positionYBuffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aPositionY);
        scaleXBuffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aScaleX);
        scaleYBuffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aScaleY);
        rotationBuffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aRotation);
        alphaBuffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aAlpha);

        color0Buffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aVaryingColor0);
        color1Buffer = new DirectVec4AttributeBuffer(INITIAL_VERTEXS, shader.aVaryingColor1);
        colorTimeBuffer = new DirectVec2AttributeBuffer(INITIAL_VERTEXS, shader.aColorTime);

        XEasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aXEasing);
        YEasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aYEasing);
        XSEasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aXSEasing);
        YSEasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aYSEasing);
        REasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aREasing);
        CEasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aCEasing);
        AEasingBuffer = new DirectIntAttributeBuffer(INITIAL_VERTEXS, shader.aAEasing);

        buffers = new DirectAttributeBuffer[]{
                anchorOffsetBuffer,
                textureCoordBuffer,
                positionXBuffer,
                positionYBuffer,
                scaleXBuffer,
                scaleYBuffer,
                alphaBuffer,
                rotationBuffer,
                color0Buffer,
                color1Buffer,
                colorTimeBuffer,
                XEasingBuffer,
                YEasingBuffer,
                XSEasingBuffer,
                YSEasingBuffer,
                REasingBuffer,
                CEasingBuffer,
                AEasingBuffer
        };
        ensureSize(INITIAL_VERTEXS, INITIAL_VERTEXS * 3);
    }

    /**
     * 用来确保可以放置的顶点数和索引数，
     * 原来的数据可能被刷新！所以请在start()前就确保绘制的数量
     */
    public void ensureSize(int vertexCount, int indiceCount) {
        indiceCount = (indiceCount / 3 + 1) * 3;
        indiceCount = Math.min(MAX_INDICES_COUNT, indiceCount);
        for (DirectAttributeBuffer b : buffers)
            b.ensureSize(vertexCount);
        if (vertexs == null || vertexCount > vertexs.length) {
            final int preCount = (vertexs == null) ? 0 : vertexs.length;
            vertexs = (vertexs == null) ? new OsbVertex[vertexCount] : Arrays.copyOf(vertexs, vertexCount);
            for (int i = preCount; i < vertexs.length; i++) {
                vertexs[i] = new OsbVertex(i);
                vertexStack.push(vertexs[i]);
            }
        }
        if (indices == null || indices.length < indiceCount) {
            indices = new int[indiceCount];
            indicesBuffer = BufferUtil.createIntBuffer(indices.length);
        }
    }

    public void addIndices(int... ind) {
        if (idcx + ind.length >= indices.length)
            flush();
        for (int i = 0; i < ind.length; i++) {
            indices[idcx] = ind[i];
            idcx++;
        }
    }

    public void getNextVertexs(OsbVertex[] ary) {
        for (int i = 0; i < ary.length; i++) {
            ary[i] = vertexStack.pop();
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
        idcx = 0;
        indicesBuffer.clear();
    }

    public void start(BaseCanvas canvas) {
        if (isRendering)
            throw new RuntimeException("A LegacyRenderer can't call start when rendering");
        isRendering = true;
        frameTime = (float) timeline.frameTime();
        frameCanvas = canvas;
        blendType = frameCanvas.getBlendSetting().getBlendType();
        resetIdxData();
    }

    public void flush() {
        if (texture == null || idcx == 0) return;
        //vao.bind();
        shader.useThis();
        shader.bindTexture(texture);
        shader.loadCamera(frameCanvas.getCamera());
        shader.uTime.loadData((float) timeline.frameTime());
        UpdateBuffer.watch();
        for (DirectAttributeBuffer b : buffers)
            b.loadToAttribute();
        indicesBuffer.position(0);
        indicesBuffer.put(indices, 0, idcx);
        indicesBuffer.position(0);
        UpdateBuffer.end();
        GLWrapped.drawElements(GLWrapped.GL_TRIANGLES, idcx, GLES20.GL_INT, indicesBuffer);
        resetIdxData();
    }

    public void end() {
        if (!isRendering)
            throw new RuntimeException("A LegacyRenderer can't call start when rendering");
        flush();
        isRendering = false;
    }

    public class OsbVertex {
        public static final int FLOAT_COUNT = 9;

        public final int index;

        public final Vec2Pointer TextureCoord;
        public final Vec2Pointer AnchorOffset;

        public final FloatMainFrameHandler PositionX, PositionY;
        public final FloatMainFrameHandler ScaleX, ScaleY;
        public final FloatMainFrameHandler Rotation, Alpha;

        public final ColorMainFrameHandler Color;

        public OsbVertex(int index) {
            this.index = index;
            TextureCoord = textureCoordBuffer.pointers[index];
            AnchorOffset = anchorOffsetBuffer.pointers[index];
            PositionX = new FloatMainFrameHandler(positionXBuffer.pointers[index], XEasingBuffer.pointers[index]);
            PositionY = new FloatMainFrameHandler(positionYBuffer.pointers[index], YEasingBuffer.pointers[index]);
            ScaleX = new FloatMainFrameHandler(scaleXBuffer.pointers[index], XSEasingBuffer.pointers[index]);
            ScaleY = new FloatMainFrameHandler(scaleYBuffer.pointers[index], YSEasingBuffer.pointers[index]);
            Rotation = new FloatMainFrameHandler(rotationBuffer.pointers[index], REasingBuffer.pointers[index]);
            Alpha = new FloatMainFrameHandler(alphaBuffer.pointers[index], AEasingBuffer.pointers[index]);
            Color = new ColorMainFrameHandler(color0Buffer.pointers[index], color1Buffer.pointers[index], colorTimeBuffer.pointers[index], CEasingBuffer.pointers[index]);
        }

        public void loadback() {
            vertexStack.add(this);
        }
    }

    public interface HandlerSelecter {
        public IFloatMainFrameHandler select(OsbVertex vertex);
    }

    public static class KeyFrameSelecters {
        public static final HandlerSelecter PositionX = new HandlerSelecter() {
            @Override
            public OsbRenderer.FloatMainFrameHandler select(OsbRenderer.OsbVertex vertex) {

                return vertex.PositionX;
            }
        };
        public static final HandlerSelecter PositionY = new HandlerSelecter() {
            @Override
            public OsbRenderer.FloatMainFrameHandler select(OsbRenderer.OsbVertex vertex) {

                return vertex.PositionY;
            }
        };
        public static final HandlerSelecter ScaleX = new HandlerSelecter() {
            @Override
            public OsbRenderer.FloatMainFrameHandler select(OsbRenderer.OsbVertex vertex) {

                return vertex.ScaleX;
            }
        };
        public static final HandlerSelecter ScaleY = new HandlerSelecter() {
            @Override
            public OsbRenderer.FloatMainFrameHandler select(OsbRenderer.OsbVertex vertex) {

                return vertex.ScaleY;
            }
        };
        public static final HandlerSelecter Rotation = new HandlerSelecter() {
            @Override
            public OsbRenderer.FloatMainFrameHandler select(OsbRenderer.OsbVertex vertex) {

                return vertex.Rotation;
            }
        };
        public static final HandlerSelecter Alpha = new HandlerSelecter() {
            @Override
            public OsbRenderer.FloatMainFrameHandler select(OsbRenderer.OsbVertex vertex) {

                return vertex.Alpha;
            }
        };
    }

    public interface IFloatMainFrameHandler {
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing);
    }

    public interface IMainFrameHandler<T> {
        public void update(T startValue, T endValue, double startTime, double duration, Easing easing);
    }

    public static class FloatMainFrameHandler implements IFloatMainFrameHandler {
        private final Vec4Pointer pointer;

        private final IntPointer easingPointer;

        public FloatMainFrameHandler(Vec4Pointer p, IntPointer e) {
            pointer = p;
            easingPointer = e;
        }

        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {
            pointer.set(startValue, endValue, (float) startTime, (float) duration);
            easingPointer.set(easing.ordinal());
        }
    }

    public static class ColorMainFrameHandler implements IMainFrameHandler<Color4> {
        private final Vec4Pointer startColor, endColor;

        private final Vec2Pointer timePointer;

        private final IntPointer easingPointer;

        public ColorMainFrameHandler(Vec4Pointer s, Vec4Pointer e, Vec2Pointer t, IntPointer es) {
            startColor = s;
            endColor = e;
            timePointer = t;
            easingPointer = es;
        }

        @Override
        public void update(Color4 startValue, Color4 endValue, double startTime, double duration, Easing easing) {
            startColor.set(startValue);
            endColor.set(endValue);
            timePointer.set((float) startTime, (float) duration);
            easingPointer.set(easing.ordinal());
        }
    }
}
