package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.Unif;
import com.edplan.framework.graphics.opengl.shader.Attr;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat2;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.buffer.Vec2Buffer;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.math.Vec4;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.batch.RectVertexBatch;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.base.IHasRectPosition;

import java.nio.FloatBuffer;

public class RectTextureShader extends Texture3DShader {
    @PointerName(Attr.RectPosition)
    @AttribType(VertexAttrib.Type.VEC2)
    public VertexAttrib vRectPosition;

    @PointerName(Unif.Padding)
    public UniformColor4 uPadding;

    @PointerName(Unif.InnerRect)
    public UniformColor4 uInnerRect;

    @PointerName(Unif.DrawingRect)
    public UniformColor4 uDrawingRect;

    protected RectTextureShader(GLProgram p) {
        super(p, true);
    }

    protected RectTextureShader(GLProgram p, boolean i) {
        super(p, i);
    }

    public void loadRectData(RectF drawingRect, Vec4 padding) {
        uPadding.loadData(padding);
        uDrawingRect.loadRect(drawingRect);
        uInnerRect.loadRect(drawingRect, padding);
    }

    public void loadRectPositions(FloatBuffer buffer) {
        vRectPosition.loadData(buffer);
    }

    @Override
    public boolean loadBatch(BaseBatch batch) {

        if (super.loadBatch(batch)) {
            if (batch instanceof IHasRectPosition) {
                loadRectPositions(((IHasRectPosition) batch).makeRectPositionBuffer());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static final RectTextureShader createRTS(String vs, String fs) {
        RectTextureShader s = new RectTextureShader(GLProgram.createProgram(vs, fs));
        return s;
    }
}
