package com.edplan.framework.graphics.shape.objs;

import com.edplan.framework.graphics.shape.IPath;
import com.edplan.framework.graphics.shape.Path;
import com.edplan.framework.graphics.shape.PathBuilder;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;

public class Circle implements PathShape {

    private Vec2 origin = new Vec2();

    private float radius;

    public Circle() {

    }

    public Circle(Vec2 origin, float radius) {
        this.origin.set(origin);
        this.radius = radius;
    }

    public void setOrigin(Vec2 origin) {
        this.origin.set(origin);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public IPath createPath() {
        Path path = new Path(48);
        PathBuilder builder = new PathBuilder(path);
        builder.moveTo(origin.x + radius, origin.y);
        builder.circleRotate(origin, FMath.Pi2);
        return path;
    }

    @Override
    public boolean isComplexShape() {
        return false;
    }
}
