package com.edplan.superutils.math;

public class Vct2<X,Y>
{
	public X x;
	public Y y;
	
	public Vct2(X x,Y y){
		this.x=x;
		this.y=y;
	}
	
	public Vct2(){
		
	}

	public Vct2 setX(X x){
		this.x=x;
		return this;
	}

	public X getX(){
		return x;
	}

	public Vct2 setY(Y y){
		this.y=y;
		return this;
	}

	public Y getY(){
		return y;
	}
	
	
	
}
