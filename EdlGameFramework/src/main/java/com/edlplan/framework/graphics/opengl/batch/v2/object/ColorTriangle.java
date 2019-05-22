package com.edlplan.framework.graphics.opengl.batch.v2.object;

import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.math.Vec3;
import com.edlplan.framework.utils.FloatRef;

public class ColorTriangle {

    public static final int FloatCount = 12;

    public static final int FloatCountPerVertex = 4;

    public Vec3 p1;
    public Vec3 p2;
    public Vec3 p3;

    public Color4 accentColor;

    public FloatRef alpha;

    public ColorTriangle(
            Vec3 ap1,
            Vec3 ap2,
            Vec3 ap3,
            FloatRef aalpha) {
        p1 = ap1;
        p2 = ap2;
        p3 = ap3;
        alpha = aalpha;
    }

    public ColorTriangle(
            Vec2 ap1,
            Vec2 ap2,
            Vec2 ap3,
            float aalpha,
            Color4 accentColor) {
        p1 = new Vec3(ap1, 0);
        p2 = new Vec3(ap2, 0);
        p3 = new Vec3(ap3, 0);
        alpha = new FloatRef();
        alpha.value = aalpha;
        this.accentColor = accentColor;
    }

    public ColorTriangle() {
        p1 = new Vec3();
        p2 = new Vec3();
        p3 = new Vec3();

        alpha = new FloatRef(1);
    }

    public void write(float[] ary, int offset) {

        float color = accentColor == null ? Color4.getPackedAlphaBit(alpha.value) : accentColor.toFloatBitABGR(alpha.value);

        ary[offset++] = p1.x;
        ary[offset++] = p1.y;
        ary[offset++] = p1.z;
        ary[offset++] = color;

        ary[offset++] = p2.x;
        ary[offset++] = p2.y;
        ary[offset++] = p2.z;
        ary[offset++] = color;

        ary[offset++] = p3.x;
        ary[offset++] = p3.y;
        ary[offset++] = p3.z;
        ary[offset] = color;
    }



}
