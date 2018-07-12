package com.edplan.framework.graphics.opengl.objs.vertex;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.graphics.opengl.objs.Vertex3D;

public class RectVertex extends TextureVertex3D
{
	public Vec2 rectPosition=new Vec2();

	public RectVertex setRectPosition(Vec2 rectPosition) {
		this.rectPosition.set(rectPosition);
		return this;
	}

	public Vec2 getRectPosition() {
		return rectPosition;
	}

	@Override
	public void set(Vertex3D v) {

		super.set(v);
		rectPosition.set(((RectVertex)v).rectPosition);
	}
	
	public void setRect(IQuad rect,float sx,float sy){
		setRectPosition(rect.getPoint(sx,sy));
	}
	
	public static RectVertex atRect(IQuad rect,float sx,float sy){
		return (new RectVertex()).setRectPosition(rect.getPoint(sx,sy));
	}
}
