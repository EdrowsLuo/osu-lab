package com.edlplan.framework.graphics.opengl.shader;

import android.opengl.GLES20;

import java.nio.Buffer;

public class VertexAttrib {
    public enum Type {
        VEC2(2, 2 * 4, GLES20.GL_FLOAT),
        VEC3(3, 3 * 4, GLES20.GL_FLOAT),
        VEC4(4, 4 * 4, GLES20.GL_FLOAT),
        FLOAT(1, 1 * 4, GLES20.GL_FLOAT),
        INT(1, 1 * 4, GLES20.GL_INT);

        private final int dataCount;

        private final int step;

        private final int dataType;

        Type(int dc, int st, int dt) {
            dataCount = dc;
            step = st;
            dataType = dt;
        }
    }

    private int handle;

    private Type type;

    private String name;

    private GLProgram program;

    private int step;

    //当使用VAO的时候会用到
    public int offset;

    VertexAttrib() {

    }

    public int getHandle() {
        return handle;
    }

    public Type getType() {
        return type;
    }

    public GLProgram getProgram() {
        return program;
    }

    public String getName() {
        return name;
    }

    public void loadData(Buffer datas) {
        if (getHandle() == -1) return;
        GLES20.glEnableVertexAttribArray(getHandle());
        GLES20.glVertexAttribPointer(
                getHandle(),
                getType().dataCount,
                getType().dataType,
                false,
                step,
                datas
        );
    }

    public void loadData(Buffer data, int numComps, int type, int step,boolean normalized) {
        if (getHandle() == -1) return;
        GLES20.glEnableVertexAttribArray(getHandle());
        GLES20.glVertexAttribPointer(
                getHandle(),
                numComps,
                type,
                normalized,
                step,
                data
        );
    }

    public void loadData(Buffer datas, int step) {
        if (getHandle() == -1) return;
        GLES20.glEnableVertexAttribArray(getHandle());
        GLES20.glVertexAttribPointer(
                getHandle(),
                getType().dataCount,
                getType().dataType,
                false,
                step,
                datas
        );
    }

    public void loadVAOData() {
        if (getHandle() == -1) return;
        GLES20.glEnableVertexAttribArray(getHandle());
        GLES20.glVertexAttribPointer(
                getHandle(),
                getType().dataCount,
                getType().dataType,
                false,
                step,
                offset
        );
    }

    @Override
    public String toString() {

        return name + ":" + type.name() + ":" + super.toString();
    }

    public static VertexAttrib findAttrib(GLProgram program, String name, Type type) {
        VertexAttrib va = new VertexAttrib();
        va.handle = GLES20.glGetAttribLocation(program.getProgramId(), name);
        va.type = type;
        va.program = program;
        va.step = type.step;
        va.name = name;
        return va;
    }

    public static VertexAttrib findAttrib(GLProgram program, String name, Type type, int step, int offset) {
        VertexAttrib va = new VertexAttrib();
        va.handle = GLES20.glGetAttribLocation(program.getProgramId(), name);
        va.type = type;
        va.program = program;
        va.step = step;
        va.offset = offset;
        va.name = name;
        return va;
    }
}
