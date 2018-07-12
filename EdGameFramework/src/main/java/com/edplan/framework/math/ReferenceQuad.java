package com.edplan.framework.math;

import com.edplan.framework.math.its.IVec2;
import com.edplan.framework.ui.Anchor;

public class ReferenceQuad
{
	public IVec2 topLeft;
	public IVec2 topRight;
	public IVec2 bottomLeft;
	public IVec2 bottomRight;
	public final IVec2[] vertexs;
	
	public ReferenceQuad(){
		vertexs=new IVec2[4];
	}
	
	public void setSource(final IVec2 tl,final IVec2 tr,final IVec2 bl,final IVec2 br){
		topLeft=tl;
		topRight=tr;
		bottomLeft=bl;
		bottomRight=br;
		vertexs[0]=tl;
		vertexs[1]=tr;
		vertexs[2]=br;
		vertexs[3]=bl;
	}
	
	public void set(final RectF rt){
		final float t=rt.getTop(),b=rt.getBottom(),l=rt.getLeft(),r=rt.getRight();
		topLeft.setX(l);
		topLeft.setY(t);
		topRight.setX(r);
		topRight.setY(t);
		bottomLeft.setX(l);
		bottomLeft.setY(b);
		bottomRight.setX(r);
		bottomRight.setY(b);
	}
	
	public void rotare(final Anchor a,final float ang){
		final IVec2 v=getPointByAnchor(a);
		rotate(v.getX(),v.getY(),ang);
	}

	public void rotate(final float ox,final float oy,final float ang){
		final float s=FMath.sin(ang);
		final float c=FMath.cos(ang);
		for(IVec2 v:vertexs){
			Vec2.rotate(v,ox,oy,s,c);
		}
	}
	
	public void flip(boolean h,boolean v){
		if(h){
			swap(topLeft,topRight);
			swap(bottomLeft,bottomRight);
		}
		if(v){
			swap(topLeft,bottomLeft);
			swap(topRight,bottomRight);
		}
	}

	private void swap(final IVec2 v1,final IVec2 v2){
		final float x=v1.getX(),y=v1.getY();
		v1.setX(v2.getX());
		v1.setY(v2.getY());
		v2.setX(x);
		v2.setY(y);
	}
	
	public IVec2 getPointByAnchor(Anchor a){
		switch(a.id/3){
			case 0:
				switch(a.id%3){
					case 0:
						return topLeft;
					case 1:
						return new Vec2((topLeft.getX()+topRight.getX())/2,(topLeft.getY()+topRight.getY())/2);
					case 2:
						return topRight;
				}
				break;
			case 1:
				switch(a.id%3){
					case 0:
						return new Vec2((topLeft.getX()+bottomLeft.getX())/2,(topLeft.getY()+bottomLeft.getY())/2);
					case 1:
						return new Vec2((topLeft.getX()+bottomLeft.getX()+topRight.getX()+bottomRight.getX())/4,(topLeft.getY()+bottomLeft.getY()+topRight.getY()+bottomRight.getY())/4);
					case 2:
						return new Vec2((topRight.getX()+bottomRight.getX())/2,(topRight.getY()+bottomRight.getY())/2);
				}
				break;
			case 2:
				switch(a.id%3){
					case 0:
						return bottomLeft;
					case 1:
						return new Vec2((bottomLeft.getX()+bottomRight.getX())/2,(bottomLeft.getY()+bottomRight.getY())/2);
					case 2:
						return bottomRight;
				}
				break;
		}
		return null;
	}
}
