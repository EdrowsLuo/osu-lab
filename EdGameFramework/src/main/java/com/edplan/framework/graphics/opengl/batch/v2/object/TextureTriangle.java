package com.edplan.framework.graphics.opengl.batch.v2.object;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.utils.FloatRef;

public class TextureTriangle {

    public static final int FloatCount = 18;

    public static final int FloatCountPerVertex = 6;

    public Vec3 p1;
    public Vec3 p2;
    public Vec3 p3;

    public Vec2 t1;
    public Vec2 t2;
    public Vec2 t3;

    public Color4 accentColor;

    public FloatRef alpha;

    public TextureTriangle(
            Vec3 ap1,
            Vec3 ap2,
            Vec3 ap3,
            Vec2 at1,
            Vec2 at2,
            Vec2 at3,
            FloatRef aalpha) {
        p1 = ap1;
        p2 = ap2;
        p3 = ap3;
        t1 = at1;
        t2 = at2;
        t3 = at3;
        alpha = aalpha;
    }

    public TextureTriangle(
            Vec2 ap1,
            Vec2 ap2,
            Vec2 ap3,
            Vec2 at1,
            Vec2 at2,
            Vec2 at3,
            float aalpha) {
        p1 = new Vec3(ap1, 0);
        p2 = new Vec3(ap2, 0);
        p3 = new Vec3(ap3, 0);
        t1 = at1;
        t2 = at2;
        t3 = at3;
        alpha = new FloatRef();
        alpha.value = aalpha;
    }

    public TextureTriangle() {
        p1 = new Vec3();
        p2 = new Vec3();
        p3 = new Vec3();

        t1 = new Vec2();
        t2 = new Vec2();
        t3 = new Vec2();
        alpha = new FloatRef(1);
    }

    public void write(float[] ary, int offset) {

        float color = accentColor == null ? Color4.getPackedAlphaBit(alpha.value) : accentColor.toFloatBitABGR(alpha.value);

        ary[offset++] = p1.x;
        ary[offset++] = p1.y;
        ary[offset++] = p1.z;
        ary[offset++] = t1.x;
        ary[offset++] = t1.y;
        ary[offset++] = color;

        ary[offset++] = p2.x;
        ary[offset++] = p2.y;
        ary[offset++] = p2.z;
        ary[offset++] = t2.x;
        ary[offset++] = t2.y;
        ary[offset++] = color;

        ary[offset++] = p3.x;
        ary[offset++] = p3.y;
        ary[offset++] = p3.z;
        ary[offset++] = t3.x;
        ary[offset++] = t3.y;
        ary[offset] = color;
    }

}
