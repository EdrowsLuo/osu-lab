package com.edplan.framework.graphics.opengl.bufferObjects;
import android.opengl.GLES20;
import java.nio.Buffer;

public class VertexArrayObject
{
	public final int id;
	
	private VertexArrayObject(){
		final int[] tmp=new int[1];
		GLES20.glGenBuffers(1,tmp,0);
		id=tmp[0];
	}
	
	public void putData(Buffer buffer,int size){
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,size,buffer,GLES20.GL_DYNAMIC_DRAW);
	}
	
	public void bind(){
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,id);
	}
	
	public void unbind(){
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
	}
	
	public static VertexArrayObject genVertexArray(){
		return new VertexArrayObject();
	}
}
