package com.edplan.framework.graphics.opengl.fast;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Quad;
import com.edplan.framework.math.ReferenceQuad;

public class FastQuad
{
	private final FastRenderer.FastVertex[] vertexs=new FastRenderer.FastVertex[4];
	
	//public final ReferenceQuad RawQuad=new ReferenceQuad();
	
	public void load(FastRenderer r){
		r.getNextVertexs(vertexs);
		//RawQuad.setSource(vertexs[0].Position,vertexs[1].Position,vertexs[2].Position,vertexs[3].Position);
	}
	
	public void setQuad(Quad q){
		vertexs[0].Position.set(q.getTopLeft());
		vertexs[1].Position.set(q.getTopRight());
		vertexs[2].Position.set(q.getBottomLeft());
		vertexs[3].Position.set(q.getBottomRight());
	}
	
	public void setTextureCoord(IQuad q){
		vertexs[0].TextureCoord.set(q.getTopLeft());
		vertexs[1].TextureCoord.set(q.getTopRight());
		vertexs[2].TextureCoord.set(q.getBottomLeft());
		vertexs[3].TextureCoord.set(q.getBottomRight());
	}
	
	public void setColor(float r,float g,float b,float a){
		for(FastRenderer.FastVertex v:vertexs){
			v.Color.set(r,g,b,a);
		}
	}
	
	public void addToRenderer(FastRenderer r){
		r.addIndices(
			vertexs[0].index,
			vertexs[1].index,
			vertexs[2].index,
			vertexs[1].index,
			vertexs[3].index,
			vertexs[2].index
		);
	}
}
