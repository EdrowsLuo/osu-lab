package com.edplan.framework.graphics.opengl.shader.uniforms;

import com.edplan.framework.graphics.opengl.shader.DataUniform;
import com.edplan.framework.graphics.opengl.shader.GLProgram;
import com.edplan.framework.math.Mat4;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.GLException;

public class UniformMat4 extends DataUniform<Mat4> {
    protected UniformMat4(int h) {
        super(h);
    }

    @Override
    public void loadData(Mat4 t) {

        if (available) GLES20.glUniformMatrix4fv(getHandle(), 1, false, t.data, 0);
    }

    public static UniformMat4 findUniform(GLProgram program, String name) {
        UniformMat4 um = new UniformMat4(GLES20.glGetUniformLocation(program.getProgramId(), name));
        //if(um.handle==-1)throw new GLException("handle "+name+" NOT found");
        return um;
    }
}
