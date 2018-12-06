package com.edplan.framework.graphics.opengl.batch.v2.mesh;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec3;

import java.util.Arrays;

public class ColorMesh {

    MeshPart colors;

    MeshPart positions;

    int size;

    public ColorMesh(int size, float[] color, float[] pos) {
        colors = new MeshPart();
        colors.data = color;
        positions = new MeshPart();
        positions.data = pos;
        this.size = size;
    }

    public int size() {
        return size;
    }

    public static class Builder {

        float[] color;

        float[] position;

        int max;

        int size;

        public Builder(int basesize) {
            max = basesize;
            color = new float[max * 4];
            position = new float[max * 3];
        }

        public Builder add(Color4 color, Vec3 pos) {
            if (color.premultiple) {
                add(
                        color.r, color.g, color.b, color.a,
                        pos.x, pos.y, pos.z
                );
            } else {
                add(
                        color.r * color.a, color.g * color.a, color.b * color.a, color.a,
                        pos.x, pos.y, pos.z
                );
            }
            return this;
        }

        public Builder add(float r, float g, float b, float a, float x, float y, float z) {

            if (size >= max) {
                max = max * 3 / 2;
                color = Arrays.copyOf(color, max * 4);
                position = Arrays.copyOf(position, max * 3);
            }

            int cof = size * 4;
            int pof = size * 3;

            color[cof++] = r;
            color[cof++] = g;
            color[cof++] = b;
            color[cof] = a;

            position[pof++] = x;
            position[pof++] = y;
            position[pof] = z;

            size++;
            return this;
        }

        public ColorMesh make() {
            return new ColorMesh(size, color, position);
        }

        public ColorMesh makeMin() {
            return new ColorMesh(size, Arrays.copyOf(color, size * 4), Arrays.copyOf(position, size * 3));
        }
    }
}
