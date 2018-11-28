package com.edplan.framework.graphics.opengl.shader.advance;

import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.shader.GLProgram;

public class GLES10Texture3DShader extends Texture3DShader {
    public GLES10Texture3DShader() {
        super(GLProgram.invalidProgram());
        if (GLWrapped.GL_VERSION != 1) {
            throw new GLException("you can only use GLES10Shader in GL_VERSION=1");
        }
    }
}
