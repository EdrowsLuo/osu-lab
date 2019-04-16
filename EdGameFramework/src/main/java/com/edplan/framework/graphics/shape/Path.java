package com.edplan.framework.graphics.shape;

import com.edplan.framework.math.Vec2;

import java.util.Arrays;

public class Path implements IEditablePath{

    private Vec2[] vArray;

    private int size;

    public Path() {
        this(3);
    }

    public Path(int buf) {
        vArray = new Vec2[buf];
        this.size = 0;
    }

    private void grow() {
        vArray = Arrays.copyOf(vArray, (int) (vArray.length * 1.5));
    }

    @Override
    public void addPoint(Vec2 vec) {

        if (size == vArray.length) {
            grow();
        }

        if (size > 0 && Vec2.lengthSquared(vec, vArray[size - 1]) < 0.01) {
            //对于过于接近的点的排除
            return;
        }

        vArray[size++] = vec;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Vec2[] buffer() {
        return vArray;
    }
}
