package com.edlplan.framework.graphics.opengl.batch.v2.object;

import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.math.IQuad;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.FloatRef;

public class AnyQuadTextureQuad extends ATextureQuad {

    public IQuad positionQuad;

    public IQuad textureQuad;

    public FloatRef alpha = new FloatRef(1);

    public Color4 accentColor;

    @Override
    public void write(float[] ary, int offset) {
        float color = accentColor == null ? Color4.getPackedAlphaBit(alpha.value) : accentColor.toFloatBitABGR(alpha.value);
        Vec2 lt = positionQuad.getTopLeft();
        Vec2 ltT = textureQuad.getTopLeft();
        ary[offset++] = lt.x;
        ary[offset++] = lt.y;
        ary[offset++] = ltT.x;
        ary[offset++] = ltT.y;
        ary[offset++] = color;

        Vec2 rt = positionQuad.getTopRight();
        Vec2 rtT = textureQuad.getTopRight();
        ary[offset++] = rt.x;
        ary[offset++] = rt.y;
        ary[offset++] = rtT.x;
        ary[offset++] = rtT.y;
        ary[offset++] = color;

        Vec2 lb = positionQuad.getBottomLeft();
        Vec2 lbT = textureQuad.getBottomLeft();
        ary[offset++] = lb.x;
        ary[offset++] = lb.y;
        ary[offset++] = lbT.x;
        ary[offset++] = lbT.y;
        ary[offset++] = color;

        Vec2 rb = positionQuad.getBottomRight();
        Vec2 rbT = textureQuad.getBottomRight();
        ary[offset++] = rb.x;
        ary[offset++] = rb.y;
        ary[offset++] = rbT.x;
        ary[offset++] = rbT.y;
        ary[offset  ] = color;
    }

}
