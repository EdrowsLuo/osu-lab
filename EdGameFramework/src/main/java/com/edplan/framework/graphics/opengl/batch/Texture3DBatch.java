package com.edplan.framework.graphics.opengl.batch;
import com.edplan.framework.graphics.opengl.batch.interfaces.ITexture3DBatch;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.Vec2;
import java.nio.FloatBuffer;

public class Texture3DBatch<T extends TextureVertex3D> extends BaseColorBatch<T> implements ITexture3DBatch<T>
{
	public Texture3DBatch(){
		
	}

	private FloatBuffer texturePointBuffer;
	@Override
	public FloatBuffer makeTexturePositionBuffer(){
		int floatSize=2*getVertexCount();
		if(texturePointBuffer==null){
			texturePointBuffer=BufferUtil.createFloatBuffer(floatSize);
		}
		texturePointBuffer.clear();
		if(texturePointBuffer.remaining()<floatSize){
			texturePointBuffer=BufferUtil.createFloatBuffer(floatSize);
			texturePointBuffer.clear();
		}
		Vec2 tmp;
		for(int i=0;i<idx;i++){
			tmp=get(i).getTexturePoint();
			texturePointBuffer.put(tmp.x).put(tmp.y);
		}
		texturePointBuffer.position(0);
		return texturePointBuffer;//texturePointBuffer;
	}
}
