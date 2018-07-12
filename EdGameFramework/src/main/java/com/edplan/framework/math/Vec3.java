package com.edplan.framework.math;

public class Vec3
{
	public static final int FLOATS=3;
	
	public float x,y,z;
	
	public Vec3(float x,float y,float z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vec3(Vec2 v2,float z){
		this.x=v2.x;
		this.y=v2.y;
		this.z=z;
	}
	
	public Vec3(Vec3 r){
		this.x=r.x;
		this.y=r.y;
		this.z=r.z;
	}
	
	public Vec3(){
		
	}
	
	public Vec3 set(Vec3 v){
		return set(v.x,v.y,v.z);
	}
	
	public Vec3 set(float x,float y,float z){
		this.x=x;
		this.y=y;
		this.z=z;
		return this;
	}
	
	public Vec3 add(Vec3 v){
		return add(v.x,v.y,v.z);
	}
	
	public Vec3 add(float ax,float ay,float az){
		this.x+=ax;
		this.y+=ay;
		this.z+=az;
		return this;
	}
	
	public Vec3 minus(Vec3 v){
		return minus(v.x,v.y,v.z);
	}
	
	public Vec3 minus(float dx,float dy,float dz){
		this.x-=dx;
		this.y-=dy;
		this.z-=dz;
		return this;
	}
	
	public Vec3 move(float offsetX,float offsetY,float offsetZ){
		x+=offsetX;
		y+=offsetY;
		z+=offsetZ;
		return this;
	}
	
	public Vec3 zoom(float zt){
		return zoom(0,0,0,zt,zt,zt);
	}

	public Vec3 zoom(float ox,float oy,float oz,float zoomTimesX,float zoomTimesY,float zoomTimesZ){
		this.x=zoomTimesX*(x-ox)+ox;
		this.y=zoomTimesY*(y-oy)+oy;
		this.z=zoomTimesZ*(z-oz)+oz;
		return this;
	}

	public Vec3 zoom(Vec3 o,float zoomTimesX,float zoomTimesY,float zoomTimesZ){
		return zoom(o.x,o.y,o.z,zoomTimesX,zoomTimesY,zoomTimesZ);
	}
	
	//顺时针，弧度
	public Vec3 rotateZ(Vec2 o,float r){
		float c=(float)Math.cos(r);
		float s=(float)Math.sin(r);
		float xr=x-o.x;
		float yr=y-o.y;
		x=o.x+xr*c-yr*s;
		y=o.y+yr*c+xr*s;
		return this;
	}
	
	public Vec3 rotateZ(float ox,float oy,float r){
		float c=(float)Math.cos(r);
		float s=(float)Math.sin(r);
		float xr=x-ox;
		float yr=y-oy;
		x=ox+xr*c-yr*s;
		y=oy+yr*c+xr*s;
		return this;
	}
	
	public float distance(){
		return distance(x,y,z);
	}
	
	public Vec3 toNormal(){
		return zoom(1/distance());
	}

	public Vec3 copy(){
		return new Vec3(this);
	}

	
	public static float distance(float x,float y,float z){
		return (float)Math.sqrt(x*x+y*y+z*z);
	}

	public static float distance(Vec3 p1,Vec3 p2){
		return distance(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z);
	}

	@Override
	public String toString() {

		return (new StringBuilder("(")).append(x).append(",").append(y).append(",").append(z).append(")").toString();
	}
}
