package com.edplan.framework.graphics.shape;

public class ShapePair implements Shape {

    public Shape dst;

    public Shape src;

    @Override
    public boolean isComplexShape() {
        return true;
    }
}
