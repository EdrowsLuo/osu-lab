package com.edplan.framework.graphics.opengl.batch.v2.mesh;

public class TextureMesh extends ColorMesh {

    MeshPart textureCoord;

    public TextureMesh(int size, float[] color, float[] pos,float[] tex) {
        super(size, color, pos);
        textureCoord = new MeshPart();
        textureCoord.data = tex;
    }

    public TextureMesh(int size) {
        this(size, new float[size * 4], new float[size * 3], new float[size * 2]);
    }
}
