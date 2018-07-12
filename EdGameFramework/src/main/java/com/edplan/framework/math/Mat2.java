package com.edplan.framework.math;

public class Mat2
{
	public static final Mat2 Identity=new Mat2(1,0,0,1);
	
	public static final int WIDTH=2;
	
	public float data[];
	
	public Mat2(){
		data=new float[4];
	}
	
	public Mat2(float v11,float v12,float v21,float v22){
		data=new float[]{v11,v12,v21,v22};
	}
	
	public Mat2(float[][] d){
		data=new float[4];
		set(d);
	}
	
	public Mat2 set(float[][] d){
		data[0]=d[0][0];
		data[1]=d[0][1];
		data[2]=d[1][0];
		data[3]=d[1][1];
		return this;
	}
	
	public Mat2(float[] d){
		data=new float[4];
		set(d);
	}
	
	public Mat2 loadIdentity(){
		return set(Identity);
	}
	
	public Mat2 set(Mat2 m){
		return set(m.data);
	}
	
	public Mat2 set(float[] d){
		data[0]=d[0];
		data[1]=d[1];
		data[2]=d[2];
		data[3]=d[3];
		return this;
	}
	
	public Mat2 set(float v11,float v12,float v21,float v22){
		data[0]=v11;
		data[1]=v12;
		data[2]=v21;
		data[3]=v22;
		return this;
	}
	
	public float get(int x,int y){
		return data[x+y*2];
	}
	
	public float detValue(){
		return data[0]*data[3]-data[1]*data[2];
	}
	
	public Mat2 post(Mat2 m){
		float[][] newData=new float[2][2];
		for(int i=0;i<WIDTH;i++){
			for(int j=0;j<WIDTH;j++){
				newData[i][j]=get(i,0)*m.get(0,j)+get(i,1)*m.get(1,j);
			}
		}
		set(newData);
		return this;
	}
	
	public Mat2 prePost(Mat2 m){
		float[][] newData=new float[2][2];
		for(int i=0;i<WIDTH;i++){
			for(int j=0;j<WIDTH;j++){
				newData[i][j]=m.get(i,0)*get(0,j)+m.get(i,1)*get(1,j);
			}
		}
		set(newData);
		return this;
	}
	
	
	
	public Mat2 copy(){ return new Mat2(data); }
	
	public static Mat2 rotation(float ang){
		float sin=FMath.sin(ang);
		float cos=FMath.cos(ang);
		return
			new Mat2(
				  cos,sin,
				 -sin,cos
			);
	}
	
	public static Mat2 zoom(float zx,float zy){
		return
			new Mat2(
				zx,0,
				0,zy
			);
	}
}
