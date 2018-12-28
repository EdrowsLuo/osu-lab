package com.edplan.framework.graphics.line;

import com.edplan.framework.graphics.opengl.batch.BaseColorBatch;
import com.edplan.framework.graphics.opengl.batch.v2.object.PackedTriangles;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureTriangle;
import com.edplan.framework.graphics.opengl.drawui.DrawInfo;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.utils.FloatRef;

import java.util.ArrayList;

public class DrawLinePath {
    private static final int MAXRES = 24;

    private static final float Z_MIDDLE = 1.0f;

    private static final float Z_SIDE = 0.0f;

    private final PackedTriangles triangles;

    public final FloatRef alpha = new FloatRef(1);

    private AbstractPath path;

    public final Vec2 textureStart = new Vec2(0, 0);

    public final Vec2 textureEnd = new Vec2(1, 1);

    public DrawLinePath(AbstractPath p) {
        triangles = new PackedTriangles(p.size() * 4);
        path = p;
        init();
    }

    private void addLineCap(Vec2 org, float theta, float thetaDiff) {
        final float step = FMath.Pi / MAXRES;

        float dir = Math.signum(thetaDiff);
        thetaDiff *= dir;
        //MLog.test.vOnce("dir","gl_test","dir: "+dir);
        int amountPoints = (int) Math.ceil(thetaDiff / step);

        if (dir < 0)
            theta += FMath.Pi;

        /* current = org + atCircle(...)*width */
        Vec3 current = new Vec3(Vec2.atCircle(theta).zoom(path.getWidth()).add(org), Z_SIDE);

        Vec3 orgAtLayer3D = new Vec3(org, Z_MIDDLE);
        for (int i = 1; i <= amountPoints; i++) {
            triangles.add(
                    new TextureTriangle(
                            orgAtLayer3D,
                            current,
                            current = new Vec3(
                                    Vec2.atCircle(theta + dir * Math.min(i * step, thetaDiff))
                                            .zoom(path.getWidth())
                                            .add(org),
                                    Z_SIDE
                            ),
                            textureStart,
                            textureEnd,
                            textureEnd,
                            alpha
                    )
            );
        }
    }

    private void addLineQuads(Vec2 ps, Vec2 pe) {
        Vec2 oth_expand = Vec2.lineOthNormal(ps, pe).zoom(path.getWidth());

        Vec3 startL = new Vec3(ps.copy().add(oth_expand), Z_SIDE);
        Vec3 startR = new Vec3(ps.copy().minus(oth_expand), Z_SIDE);
        Vec3 endL = new Vec3(pe.copy().add(oth_expand), Z_SIDE);
        Vec3 endR = new Vec3(pe.copy().minus(oth_expand), Z_SIDE);
        Vec3 start = new Vec3(ps, Z_SIDE);
        Vec3 end = new Vec3(pe, Z_SIDE);

        triangles.add(
                new TextureTriangle(
                        start,
                        end,
                        endL,
                        textureStart,
                        textureStart,
                        textureEnd,
                        alpha
                )
        );

        triangles.add(
                new TextureTriangle(
                        start,
                        endL,
                        startL,
                        textureStart,
                        textureEnd,
                        textureEnd,
                        alpha
                )
        );

        triangles.add(
                new TextureTriangle(
                        start,
                        endR,
                        end,
                        textureStart,
                        textureEnd,
                        textureStart,
                        alpha
                )
        );

        triangles.add(
                new TextureTriangle(
                        start,
                        startR,
                        endR,
                        textureStart,
                        textureEnd,
                        textureEnd,
                        alpha
                )
        );
    }

    private void init() {
        if (path.size() < 2) {
            if (path.size() == 1) {
                addLineCap(path.get(0), FMath.Pi, FMath.Pi);
                addLineCap(path.get(0), 0, FMath.Pi);
                return;
            } else {
                return;
                //throw new RuntimeException("Path must has at least 1 point");
            }
        }

        float theta = Vec2.calTheta(path.get(0), path.get(1));
        addLineCap(path.get(0), theta + FMath.PiHalf, FMath.Pi);
        addLineQuads(path.get(0), path.get(1));
        if (path.size() == 2) {
            addLineCap(path.get(1), theta - FMath.PiHalf, FMath.Pi);
            return;
        }
        Vec2 nowPoint = path.get(1);
        Vec2 nextPoint;
        float preTheta = theta;
        float nextTheta;
        int max_i = path.size();
        for (int i = 2; i < max_i; i++) {
            nextPoint = path.get(i);
            nextTheta = Vec2.calTheta(nowPoint, nextPoint);
            addLineCap(nowPoint, preTheta - FMath.PiHalf, nextTheta - preTheta);
            addLineQuads(nowPoint, nextPoint);
            nowPoint = nextPoint;
            preTheta = nextTheta;
        }
        addLineCap(path.get(max_i - 1), preTheta - FMath.PiHalf, FMath.Pi);
    }


}
