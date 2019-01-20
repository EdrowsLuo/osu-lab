package com.edplan.framework.graphics.opengl.objs;

import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;

@Deprecated
public class Vertex3D {
    public static final Color4 DEF_COLOR = Color4.rgba(1.0f, 1.0f, 1.0f, 1.0f);

    public static final Vec3 DEF_POSITION = new Vec3(0, 0, 0);

    public Vec3 position;

    public Color4 color;

    public Vertex3D() {
        position = DEF_POSITION.copy();
        color = DEF_COLOR.copyNew();
    }

    public Vertex3D(float x, float y, float z, Color4 color) {
        this.position = new Vec3(x, y, z);
        this.color = color.copyNew();
    }

    public Vertex3D(Vec3 p) {
        this.position = p.copy();
        color = DEF_COLOR.copyNew();
    }

    public Vertex3D(float x, float y, float z) {
        this(new Vec3(x, y, z));
    }

    public void set(Vertex3D t) {
        setColor(t.getColor());
        setPosition(t.getPosition());
    }

    public Vertex3D setColor(Color4 color) {
        this.color.set(color.toPremultipled());
        return this;
    }

    public Color4 getColor() {
        return color;
    }

    public Vertex3D setPosition(Vec3 position) {
        this.position.set(position);
        return this;
    }

    public Vertex3D setPosition(Vec2 v, float z) {
        this.position.set(v.x, v.y, z);
        return this;
    }

    public Vec3 getPosition() {
        return position;
    }

    public static Vertex3D atPosition(float x, float y, float z) {
        return new Vertex3D(x, y, z);
    }

    public static Vertex3D atPosition(Vec3 v) {
        return new Vertex3D(v);
    }

    @Override
    public String toString() {

        return (new StringBuilder())
                .append("pos: ").append(position.toString()).append("\n")
                .append("color: ").append(color.toString()).append("\n").toString();
    }
}
