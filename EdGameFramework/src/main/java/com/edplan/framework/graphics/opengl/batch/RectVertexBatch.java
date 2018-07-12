package com.edplan.framework.graphics.opengl.batch;

import com.edplan.framework.graphics.opengl.batch.base.IHasRectPosition;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.objs.vertex.RectVertex;
import com.edplan.framework.math.Vec2;
import java.nio.FloatBuffer;

public class RectVertexBatch<T extends RectVertex> extends Texture3DBatch<T> implements IHasRectPosition
{
	/*
	private Vec2Buffer rectPositionBuffer=new Vec2Buffer();
	@Override
	public FloatBuffer makeRectPositionBuffer(){
		rectPositionBuffer.clear();
		for(T t:vertexs){
			rectPositionBuffer.add(t.getRectPosition());
		}
		return null;//rectPositionBuffer;
	}
	*/
	
	private FloatBuffer rectPositionBuffer;
	@Override
	public FloatBuffer makeRectPositionBuffer(){
		int floatSize=2*getVertexCount();
		if(rectPositionBuffer==null){
			rectPositionBuffer=BufferUtil.createFloatBuffer(floatSize);
		}
		rectPositionBuffer.clear();
		if(rectPositionBuffer.remaining()<floatSize){
			rectPositionBuffer=BufferUtil.createFloatBuffer(floatSize);
			rectPositionBuffer.clear();
		}
		Vec2 tmp;
		for(int i=0;i<idx;i++){
			tmp=get(i).getTexturePoint();
			rectPositionBuffer.put(tmp.x).put(tmp.y);
		}
		rectPositionBuffer.position(0);
		return rectPositionBuffer;//rectPositionBuffer;
	}
}
