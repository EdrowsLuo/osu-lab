package com.edplan.framework.graphics.opengl.batch.v2.mesh;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec3;

import java.util.Arrays;

public class ColorMesh implements Mesh{

    MeshPart colors;

    MeshPart positions;

    private int size;

    public ColorMesh(int size, float[] color, float[] pos) {
        this.size = size;
        colors = new MeshPart();
        colors.data = color;
        positions = new MeshPart();
        positions.data = pos;

    }

    public ColorMesh(int size) {
        this(size, new float[size * 4], new float[size * 3]);
    }

    public int size() {
        return size;
    }
}
