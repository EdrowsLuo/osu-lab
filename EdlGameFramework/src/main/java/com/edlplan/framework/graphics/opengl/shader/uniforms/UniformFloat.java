package com.edlplan.framework.graphics.opengl.shader.uniforms;

import android.opengl.GLES20;

import com.edlplan.framework.graphics.opengl.shader.DataUniform;
import com.edlplan.framework.graphics.opengl.shader.GLProgram;

public class UniformFloat extends DataUniform<Float> {
    private UniformFloat(int h) {
        super(h);
    }

    @Override
    public void loadData(Float t) {
        if (available) GLES20.glUniform1f(getHandle(), t);
    }

    public static UniformFloat findUniform(GLProgram program, String name) {
        UniformFloat um = new UniformFloat(GLES20.glGetUniformLocation(program.getProgramId(), name));
        //if(um.handle==-1)throw new GLException("handle "+name+" NOT found");
        return um;
    }
}
