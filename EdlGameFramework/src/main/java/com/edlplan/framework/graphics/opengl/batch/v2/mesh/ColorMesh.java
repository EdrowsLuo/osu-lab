package com.edlplan.framework.graphics.opengl.batch.v2.mesh;

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
