package com.edplan.framework.graphics.opengl.shader.uniforms;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.shader.DataUniform;
import com.edplan.framework.graphics.opengl.shader.GLProgram;

public class UniformSample2D extends DataUniform<GLTexture> {
    private int textureIndex;

    protected UniformSample2D(int h, int idx) {
        super(h);
        this.textureIndex = idx;
    }

    @Override
    public void loadData(GLTexture t) {

        if (available) t.bind(textureIndex);
    }

    public static UniformSample2D findUniform(GLProgram program, String name, int index) {
        UniformSample2D u = new UniformSample2D(GLES20.glGetUniformLocation(program.getProgramId(), name), index);
        //if(u.handle==-1)throw new GLException("handle "+name+" NOT found");
        return u;
    }
}
