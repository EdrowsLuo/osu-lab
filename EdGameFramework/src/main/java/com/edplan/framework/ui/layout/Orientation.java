package com.edplan.framework.ui.layout;

/**
 *描述组件布局朝向
 */
public class Orientation
{
	public static final int MAIN_LEFT_BITS=2;
	
	public static final int RAW_LEFT_BITS=5;
	
	public static final int MAIN_ORIENTATION_MASK=(1<<MAIN_LEFT_BITS)-1;
	
	public static final int DIRECTION_LEFT_BITS=RAW_LEFT_BITS+MAIN_LEFT_BITS;
	
	public static final int DIRECTION_HORIZON=0;
	
	public static final int DIRECTION_VERTICAL=1;
	
	public static final int HORIZON_MASK=((1<<(RAW_LEFT_BITS))-1)<<MAIN_LEFT_BITS;
	
	public static final int VERTICAL_MASK=HORIZON_MASK<<RAW_LEFT_BITS;
	
	
	public static final int DIRECTION_BIG2SMALL=1;

	public static final int DIRECTION_NONE=0;

	public static final int DIRECTION_SMALL2BIG=2;
	
	public static final int DIRECTION_L2R=(DIRECTION_BIG2SMALL<<MAIN_LEFT_BITS)|DIRECTION_HORIZON;

	public static final int DIRECTION_R2L=(DIRECTION_SMALL2BIG<<MAIN_LEFT_BITS)|DIRECTION_HORIZON;

	public static final int DIRECTION_NONE_HORIZON=(DIRECTION_NONE<<MAIN_LEFT_BITS)|DIRECTION_HORIZON;

	public static final int DIRECTION_T2B=(DIRECTION_BIG2SMALL<<DIRECTION_LEFT_BITS)|DIRECTION_VERTICAL;

	public static final int DIRECTION_B2T=(DIRECTION_SMALL2BIG<<DIRECTION_LEFT_BITS)|DIRECTION_VERTICAL;

	public static final int DIRECTION_NONE_VERTICAL=(DIRECTION_NONE<<DIRECTION_LEFT_BITS)|DIRECTION_VERTICAL;
	
	public static int getMainOrientation(int flag){
		return flag&MAIN_ORIENTATION_MASK;
	}
}
