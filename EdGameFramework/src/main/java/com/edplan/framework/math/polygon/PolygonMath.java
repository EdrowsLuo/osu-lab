package com.edplan.framework.math.polygon;

import com.edplan.framework.math.Vec2;

public class PolygonMath {

    /**
     * 将一个凸多边形划分为三角形
     * @param points 原多边形
     * @param dstArray 目标数组，按照三个顶点一个三角形的方式储存，长度至少为 offset + 3*(点数 - 2)
     */
    public static void divideConvexPolygon(Vec2[] points, int offsetP, int l, Vec2[] dstArray, int offset) {
        Vec2 start = points[0];
        int end = offsetP + l;
        for (int i = 2 + offsetP; i < end; i++) {
            dstArray[offset++] = start;
            dstArray[offset++] = points[i - 1];
            dstArray[offset++] = points[i];
        }
    }

    public static void divideConvexPolygon(Vec2[] points, Vec2[] dstArray) {
        divideConvexPolygon(points, 0, points.length, dstArray, 0);
    }

        public enum DivideType {
        ConvexPolygon, //凸多边形
        AnyPolygon
    }
}
