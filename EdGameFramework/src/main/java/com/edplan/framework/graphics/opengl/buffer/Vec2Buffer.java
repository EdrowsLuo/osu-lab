package com.edplan.framework.graphics.opengl.buffer;

import com.edplan.framework.math.Vec2;

import java.nio.FloatBuffer;

public class Vec2Buffer extends BaseBuffer<Vec2> {

    public static final int FLOAT_SIZE = 8;

    public Vec2Buffer(int size) {
        super(size);
    }

    public Vec2Buffer(Vec2[] ary) {
        super(ary);
    }

    public Vec2Buffer() {
        super(6);
    }

    @Override
    public int getFloatSize() {

        return FLOAT_SIZE;
    }

    @Override
    protected void addToBuffer(FloatBuffer fb, Vec2 t) {

        fb.put(t.x).put(t.y);
    }
}


/*
public class Vec2Buffer
{
	public static final int RAW_SIZE=2;
	public static final int RAW_BYTE_SIZE=4;
	
	public List<Vec2> bufferList;
	
	public FloatBuffer buffer;

	public Vec2Buffer(){
		initial(12);
	}
	
	public Vec2Buffer(int size){
		initial(size);
	}

	public void initial(int size){
		bufferList=new ArrayList<Vec2>(size);
	}

	public Vec2Buffer add(Vec2 t){
		bufferList.add(t);
		return this;
	}
	
	public void clear(){
		bufferList.clear();
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
		Vec2 t;
		int end=offset+length;
		for(int i=offset;i<end;i++){
			t=bufferList.get(i);
			buffer.put(t.x).put(t.y);
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
		for(Vec2 t:bufferList){
			buffer.put(t.x).put(t.y);
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
