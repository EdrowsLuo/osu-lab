package com.edplan.framework.math.polygon;

import com.edplan.framework.math.Vec2;

public class PolygonMath {

    /**
     * 将一个凸多边形划分为三角形
     * @param points 原多边形
     * @param dstArray 目标数组，按照三个顶点一个三角形的方式储存
     */
    public static void divideConvexPolygon(Vec2[] points, Vec2[] dstArray, int offset) {
        Vec2 start = points[0];
        int end = points.length;
        for (int i = 2; i < end; i++) {
            dstArray[offset++] = start;
            dstArray[offset++] = points[i - 1];
            dstArray[offset++] = points[i];
        }
    }

    public enum DivideType {
        ConvexPolygon, //凸多边形
        AnyPolygon
    }
}
