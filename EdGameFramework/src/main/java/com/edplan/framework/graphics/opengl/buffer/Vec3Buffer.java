package com.edplan.framework.graphics.opengl.buffer;

import com.edplan.framework.math.Vec3;

import java.nio.FloatBuffer;

public class Vec3Buffer extends BaseBuffer<Vec3> {

    public static final int FLOAT_SIZE = 3;

    public Vec3Buffer() {
        super(6);
    }

    public Vec3Buffer(Vec3[] ary) {
        super(ary);
    }

    public Vec3Buffer(int size) {
        super(size);
    }

    @Override
    public int getFloatSize() {

        return FLOAT_SIZE;
    }

    @Override
    protected void addToBuffer(FloatBuffer fb, Vec3 t) {

        fb.put(t.x).put(t.y).put(t.z);
    }
}


/*
public class Vec3Buffer
{
	public static final int RAW_SIZE=3;
	public static final int RAW_BYTE_SIZE=4;
	
	public List<Vec3> bufferList;

	public FloatBuffer buffer;
	
	public Vec3Buffer(){
		initial();
	}
	
	public void initial(){
		bufferList=new ArrayList<Vec3>();
	}
	
	public Vec3Buffer add(Vec3 t){
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
		Vec3 t;
		int end=offset+length;
		for(int i=offset;i<end;i++){
			t=bufferList.get(i);
			buffer.put(t.x).put(t.y).put(t.z);
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
		for(Vec3 t:bufferList){
			buffer.put(t.x).put(t.y).put(t.z);
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
