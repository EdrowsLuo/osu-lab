package com.edplan.framework.ui;
import com.edplan.framework.math.Vec2;

public class Anchor
{
	public static final int ID_CUSTOM=9;
	
	public static final int SPECIAL_ANCHOR_COUNT=9;
	
	public static final Anchor TopLeft=new Anchor(0,0,0);
	public static final Anchor TopCenter=new Anchor(0.5f,0,1);
	public static final Anchor TopRight=new Anchor(1,0,2);
	public static final Anchor CenterLeft=new Anchor(0,0.5f,3);
	public static final Anchor Center=new Anchor(0.5f,0.5f,4);
	public static final Anchor CenterRight=new Anchor(1,0.5f,5);
	public static final Anchor BottomLeft=new Anchor(0,1,6);
	public static final Anchor BottomCenter=new Anchor(0.5f,1,7);
	public static final Anchor BottomRight=new Anchor(1,1,8);
	
	
	final private Vec2 value;
	final public int id;
	final public int xs,ys;
	
	public Anchor(float x,float y){
		value=new Vec2(x,y);
		id=8;
		xs=3;
		ys=3;
	}
	
	protected Anchor(float x,float y,int id){
		value=new Vec2(x,y);
		this.id=id;
		xs=Math.round(x*2);
		ys=Math.round(y*2);
	}
	
	public float x(){
		return value.x;
	}
	
	public float y(){
		return value.y;
	}
}
