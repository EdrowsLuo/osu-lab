package com.edplan.framework.graphics.opengl.shader;

import android.opengl.GLES20;
import android.util.Log;

import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.interfaces.Recycleable;
import com.edplan.framework.graphics.opengl.shader.compile.Preprocessor;

public class GLProgram implements Recycleable {
    private GLShader vertexShader;

    private GLShader fragmentShader;

    private int id;

    private int noPreMultipleId;

    protected GLProgram(GLShader vs, GLShader fs, int id) {
        this.vertexShader = vs;
        this.fragmentShader = fs;
        this.id = id;
    }

    protected GLProgram() {

    }

    public void linkNoPreMultipleShader(GLShader nmpfs) {
        GLProgram p = linkShader(vertexShader, nmpfs);
        this.noPreMultipleId = p.getProgramId();
    }

    public GLShader getVertexShader() {
        return vertexShader;
    }

    public GLShader getFragmentShader() {
        return fragmentShader;
    }

    public int getProgramId() {
        return id;
    }

    public void useThis() {
        GLES20.glUseProgram(getProgramId());
    }

    @Override
    public void recycle() {

        GLES20.glDeleteProgram(getProgramId());
    }

    public static GLProgram linkShader(GLShader vs, GLShader fs) {
        if (vs.getType() != GLShader.Type.Vertex) {
            throw new IllegalArgumentException("param@vs should be Type.Vertex");
        }
        if (fs.getType() != GLShader.Type.Fragment) {
            throw new IllegalArgumentException("param@fs should be Type.Fragment");
        }
        //创建程序
        int program = GLES20.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program, vs.getShaderId());
            GLWrapped.checkGlError("glAttachShader");
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, fs.getShaderId());
            GLWrapped.checkGlError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program状态的数组
            int[] linkStatus = new int[1];
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
                throw new GLException("err link GLProgram");
            }
        } else {
            Log.e("ES20_ERROR", "log:" + GLES20.glGetProgramInfoLog(program));
            throw new GLException("err create GLProgram");
        }

        return new GLProgram(vs, fs, program);
    }

    public static GLProgram createProgram(String vs, String fs) {
        Preprocessor vsc = new Preprocessor(vs).compile();
        Preprocessor fsc = new Preprocessor(fs).compile();
        vs = vsc.getResult();
        fs = fsc.getResult();
        //if(vsc.hasChange())Log.v("shader","vs:\n"+vs);
        //if(fsc.hasChange())Log.v("shader","fs:\n"+fs);
        return linkShader(
                GLShader.loadShader(GLShader.Type.Vertex, vs),
                GLShader.loadShader(GLShader.Type.Fragment, fs));
    }

    public static GLProgram invalidProgram() {
        return new GLProgram();
    }
}
