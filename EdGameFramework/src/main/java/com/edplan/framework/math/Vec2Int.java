package com.edplan.framework.math;

public class Vec2Int
{
	public static final Vec2Int BASE_POINT=new Vec2Int(0,0);

	public int x,y;

	public Vec2Int(){

	}

	public Vec2Int(int x,int y){
		this.x=x;
		this.y=y;
	}

	public Vec2Int(Vec2Int res){
		this.x=res.x;
		this.y=res.y;
	}

	public Vec2Int set(int x,int y){
		this.x=x;
		this.y=y;
		return this;
	}

	public Vec2Int add(Vec2Int v){
		return add(v.x,v.y);
	}

	public Vec2Int add(int ax,int ay){
		this.x+=ax;
		this.y+=ay;
		return this;
	}

	public Vec2Int minus(Vec2Int v){
		return minus(v.x,v.y);
	}

	public Vec2Int minus(int dx,int dy){
		this.x-=dx;
		this.y-=dy;
		return this;
	}

	public Vec2Int move(int offsetX,int offsetY){
		x+=offsetX;
		y+=offsetY;

		return this;
	}

	public Vec2Int divide(int dx,int dy){
		this.x/=dx;
		this.y/=dy;
		return this;
	}

	public Vec2Int divide(int d){
		return divide(d,d);
	}

	public Vec2Int zoom(int zt){
		return zoom(0,0,zt,zt);
	}

	public Vec2Int zoom(int ox,int oy,int zoomTimesX,int zoomTimesY){
		this.x=zoomTimesX*(x-ox)+ox;
		this.y=zoomTimesY*(y-oy)+oy;
		return this;
	}

	public Vec2Int zoom(Vec2Int o,int zoomTimesX,int zoomTimesY){
		return zoom(o.x,o.y,zoomTimesX,zoomTimesY);
	}
	
	public int dot(Vec2Int v){
		return this.x*v.x+this.y*v.y;
	}

	public float length(){
		return length(x,y);
	}

	public int lengthSquared(){
		return lengthSquared(x,y);
	}

	public float theta(){
		return FMath.atan2(y,x);
	}

	//public int thetaX(){
	//	return -theta();
	//}

	public Vec2Int copy(){
		return new Vec2Int(this);
	}

	public Vec3 toVec3(int z){
		return new Vec3(this.x,this.y,z);
	}

	public static int lengthSquared(int x,int y){
		return x*x+y*y;
	}

	public static int lengthSquared(Vec2Int v1,Vec2Int v2){
		return lengthSquared(v1.x-v2.x,v1.y-v2.y);
	}

	public static float length(int x,int y){
		return FMath.sqrt(lengthSquared(x,y));
	}

	public static float length(Vec2Int p1,Vec2Int p2){
		return length(p1.x-p2.x,p1.y-p2.y);
	}

	public static float calTheta(Vec2Int start,Vec2Int end){
		return FMath.atan2(end.y-start.y,end.x-start.x);
	}

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Vec2Int){
			Vec2Int v=(Vec2Int)obj;
			return v.x==x&&v.y==y;
		}else{
			return false;
		}
	}

	@Override
	public String toString() {

		return (new StringBuilder("(")).append(x).append(",").append(y).append(")").toString();
	}
}
