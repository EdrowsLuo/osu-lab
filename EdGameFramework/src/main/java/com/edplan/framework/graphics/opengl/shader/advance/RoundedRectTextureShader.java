package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformMat2;
import com.edplan.framework.graphics.opengl.shader.Unif;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec4;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.batch.RectVertexBatch;

public class RoundedRectTextureShader extends RectTextureShader {
    public static final float DEFAULT_GLOW_FACTOR = 0.5f;

    @PointerName(Unif.RoundedRidaus)
    public UniformFloat uRoundedRidaus;

    @PointerName(Unif.RoundedInner)
    public UniformColor4 uRoundedInner;

    @PointerName(Unif.GlowFactor)
    public UniformFloat uGlowFactor;

    @PointerName(Unif.GlowColor)
    public UniformColor4 uGlowColor;

    protected RoundedRectTextureShader(GLProgram p) {
        super(p);
    }

    public void loadRectData(RectF rect, Vec4 padding, float radius, Color4 glowColor, float glowFactor) {
        super.loadRectData(rect, padding);
        uRoundedRidaus.loadData(radius);
        uRoundedInner.loadRect(rect, padding.copyNew().add(radius));
        uGlowFactor.loadData(glowFactor);
        uGlowColor.loadData(glowColor);
    }

    public void loadRectData(RectF rect, GLPaint paint) {
        loadRectData(rect, paint.getPadding(), paint.getRoundedRadius(), paint.getGlowColor(), paint.getGlowFactor());
    }

    @Override
    public void loadRectData(RectF drawingRect, Vec4 padding) {

        loadRectData(drawingRect, padding, 0, Color4.Alpha, DEFAULT_GLOW_FACTOR);
    }

    public static final RoundedRectTextureShader createRRTS(String vs, String fs) {
        RoundedRectTextureShader s = new RoundedRectTextureShader(GLProgram.createProgram(vs, fs));
        return s;
    }
}
