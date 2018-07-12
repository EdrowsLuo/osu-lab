package com.edplan.framework.graphics.opengl.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import com.edplan.framework.graphics.opengl.objs.Color4;
import android.util.Log;

public class Color4Buffer extends BaseBuffer<Color4>
{

	public static final int FLOAT_SIZE=4;

	public Color4Buffer(){
		super(6);
	}
	
	public Color4Buffer(Color4[] ary){
		super(ary);
	}

	public Color4Buffer(int size){
		super(size);
	}

	@Override
	public int getFloatSize() {

		return FLOAT_SIZE;
	}

	@Override
	protected void addToBuffer(FloatBuffer fb,Color4 t) {

		fb.put(t.r).put(t.g).put(t.b).put(t.a);
	}
}

/*
public class Color4Buffer 
{
	public static final int RAW_SIZE=4;
	public static final int RAW_BYTE_SIZE=4;
	
	public ArrayList<Color4> bufferList;
	
	public FloatBuffer buffer;

	public Color4Buffer(){
		initial();
	}

	public void initial(){
		bufferList=new ArrayList<Color4>();
	}
	
	public void clear(){
		bufferList.clear();
		//if(buffer!=null)buffer.clear();
	}

	public Color4Buffer add(Color4 t){
		bufferList.add(t);
		return this;
	}
	
	int latestListSize;
	
	public void ensureBufferSize(int size){
		if(latestListSize<size){
			latestListSize=size;
			if(buffer!=null)buffer.clear();
			buffer=createFloatBuffer(size*RAW_SIZE);
		}
	}

	public FloatBuffer makeBuffer(int offset,int length){
		int position=(latestListSize-length)*RAW_SIZE;
		buffer.position(position);
		Color4 t;
		int end=offset+length;
		for(int i=offset;i<end;i++){
			t=bufferList.get(i);
			buffer.put(t.r).put(t.g).put(t.b).put(t.a);
		}
		buffer.position(position);
		return buffer;
	}
	
	public FloatBuffer makeBuffer(){
		if(bufferList.size()<=latestListSize&&buffer!=null){
			return makeBuffer(0,bufferList.size());
		}

		if(buffer!=null)buffer.clear();
		buffer=createFloatBuffer(bufferList.size()*RAW_SIZE);
		for(Color4 t:bufferList){
			buffer.put(t.r).put(t.g).put(t.b).put(t.a);
		}
		buffer.position(0);
		latestListSize=bufferList.size();
		return buffer;
	}

	public static FloatBuffer createFloatBuffer(int floatCount){
		ByteBuffer bb=ByteBuffer.allocateDirect(floatCount*RAW_BYTE_SIZE);
		bb.order(ByteOrder.nativeOrder());
		return bb.asFloatBuffer();
	}
}
*/
