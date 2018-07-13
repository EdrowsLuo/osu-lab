package com.edplan.framework.graphics.opengl.shader.uniforms;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.shader.DataUniform;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec4;

public class UniformColor4 extends DataUniform<Color4> {

    protected UniformColor4(int h) {
        super(h);
    }

    public void loadRect(RectF rect) {
        if (available)
            GLES20.glUniform4f(getHandle(), rect.getX1(), rect.getY1(), rect.getX2(), rect.getY2());
    }

    public void loadRect(RectF rect, float padding) {
        if (available)
            GLES20.glUniform4f(getHandle(), rect.getX1() + padding, rect.getY1() + padding, rect.getX2() - padding, rect.getY2() - padding);
    }

    public void loadRect(RectF rect, Vec4 padding) {
        loadRect(rect.copy().padding(padding));
    }

    public void loadData(float r, float g, float b, float a) {
        if (available) {
            GLES20.glUniform4f(getHandle(), r, g, b, a);
        }
    }

    @Override
    public void loadData(Color4 t) {

        if (available)
            if (!t.premultiple) {
                GLES20.glUniform4f(getHandle(), t.r * t.a, t.g * t.a, t.b * t.a, t.a);
            } else {
                GLES20.glUniform4f(getHandle(), t.r, t.g, t.b, t.a);
            }
    }

    public static UniformColor4 findUniform(GLProgram program, String name) {
        UniformColor4 um = new UniformColor4(GLES20.glGetUniformLocation(program.getProgramId(), name));
        //if(um.handle==-1)throw new GLException("handle "+name+" NOT found");
        return um;
    }
}
