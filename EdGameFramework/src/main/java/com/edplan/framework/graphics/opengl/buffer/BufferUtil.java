package com.edplan.framework.graphics.opengl.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class BufferUtil 
{
	/**
	 *默认顶点顺序
	 *3       2
	 * ┌──┐
	 * └──┘
	 *0       1
	 */
	
	
	public static final ShortBuffer STD_RECT_INDICES_BUFFER;
	
	public static final FloatBuffer STD_1X1_POSITION_BUFFER;
	
	public static final FloatBuffer STD_RECT_COLOR_ONE_BUFFER;
	
	static{
		STD_RECT_INDICES_BUFFER=createShortBuffer(6);
		STD_RECT_INDICES_BUFFER.position(0);
		STD_RECT_INDICES_BUFFER.put(new short[]{3,2,0,2,1,0});
		STD_RECT_INDICES_BUFFER.position(0);
		
		STD_1X1_POSITION_BUFFER=createFloatBuffer(2*4);
		STD_1X1_POSITION_BUFFER.position(0);
		STD_1X1_POSITION_BUFFER.put(new float[]{
			0,1,
			1,1,
			1,0,
			0,0
		});
		STD_1X1_POSITION_BUFFER.position(0);
		
		STD_RECT_COLOR_ONE_BUFFER=BufferUtil.createFloatBuffer(4*4);
		STD_RECT_COLOR_ONE_BUFFER.position(0);
		Color4.ONE.put2buffer(STD_RECT_COLOR_ONE_BUFFER);
		Color4.ONE.put2buffer(STD_RECT_COLOR_ONE_BUFFER);
		Color4.ONE.put2buffer(STD_RECT_COLOR_ONE_BUFFER);
		Color4.ONE.put2buffer(STD_RECT_COLOR_ONE_BUFFER);
		STD_RECT_COLOR_ONE_BUFFER.position(0);
	}
	
	public static FloatBuffer createFloatBuffer(int floatCount){
		ByteBuffer bb=ByteBuffer.allocateDirect(floatCount*4);
		bb.order(ByteOrder.nativeOrder());
		return bb.asFloatBuffer();
	}
	
	public static ShortBuffer createShortBuffer(int shortCount){
		ByteBuffer bb=ByteBuffer.allocateDirect(shortCount*2);
		bb.order(ByteOrder.nativeOrder());
		return bb.asShortBuffer();
	}
	
	public static IntBuffer createIntBuffer(int intCount){
		ByteBuffer bb=ByteBuffer.allocateDirect(intCount*4);
		bb.order(ByteOrder.nativeOrder());
		return bb.asIntBuffer();
	}
}
