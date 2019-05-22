package com.edlplan.framework.math.polygon;

import com.edlplan.framework.math.Vec2;

import java.util.List;

public class Polygon {

    public Vec2[] points;

    public Polygon(List<Vec2> points, boolean clone) {
        this.points = new Vec2[points.size()];
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = clone ? points.get(i).copy() : points.get(i);
        }
    }

    public Polygon(List<Vec2> points) {
        this(points, true);
    }
}
