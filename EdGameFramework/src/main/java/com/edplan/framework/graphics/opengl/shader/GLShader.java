package com.edplan.framework.graphics.opengl.shader;

import android.opengl.GLES20;
import android.util.Log;
import com.edplan.framework.graphics.opengl.GLException;

public class GLShader 
{
	private int id;
	
	private Type type;
	
	private GLShader(Type type,int id){
		this.type=type;
		this.id=id;
	}
	
	public Type getType(){
		return type;
	}
	
	public int getShaderId(){
		return id;
	}

	public static GLShader loadShader(Type shaderType,String source) {
	    //创建一个新shader
        int shader = GLES20.glCreateShader(shaderType.getFlag());
        //若创建成功则加载shader
        if (shader != 0) {
        	//加载shader的源代码
            GLES20.glShaderSource(shader, source);
            //编译shader
            GLES20.glCompileShader(shader);
            //存放编译成功shader数量的数组
            int[] compiled = new int[1];
            //获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//若编译失败则显示错误日志并删除此shader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                throw new GLException("err complie shader: "+source.substring(0,Math.min(source.length(),500)));
            }  
        }
        return new GLShader(shaderType,shader);
    }
	
	public enum Type{
		Vertex(GLES20.GL_VERTEX_SHADER),
		Fragment(GLES20.GL_FRAGMENT_SHADER);
		private final int flag;
		Type(int flag){
			this.flag=flag;
		}
		
		public int getFlag(){
			return flag;
		}
	}
}
