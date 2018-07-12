package com.edplan.framework.ui.layout;

public class MarginLayoutParam extends EdLayoutParam
{
	public float marginLeft;
	public float marginRight;
	public float marginTop;
	public float marginBottom;
	
	public MarginLayoutParam(){
		
	}
	
	public MarginLayoutParam(EdLayoutParam l){
		super(l);
	}
	
	public MarginLayoutParam(MarginLayoutParam l){
		super(l);
		marginRight=l.marginRight;
		marginTop=l.marginTop;
		marginLeft=l.marginLeft;
		marginBottom=l.marginBottom;
	}
	
	public void postMargin(float m){
		marginBottom=Math.max(m,marginBottom);
		marginLeft=Math.max(m,marginLeft);
		marginRight=Math.max(m,marginRight);
		marginTop=Math.max(m,marginTop);
	}
	
	public float getMarginHorizon(){
		return marginRight+marginLeft;
	}
	
	public float getMarginVertical(){
		return marginTop+marginBottom;
	}
}
