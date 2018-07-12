package com.edplan.framework.ui.layout;
import com.edplan.framework.ui.ViewConfiguration;

public class Param{
	public static final int SHIFT_SIZE=32;
	public static final long SIZE_MASK=(1L<<SHIFT_SIZE)-1;
	public static final long MODE_MASK=((1L<<10)-1)<<SHIFT_SIZE;

	public static final int NONE=0;
	public static final int MATCH_PARENT=1;
	public static final int WRAP_CONTENT=2;
	//当前方向的父类大小的倍率
	public static final int SCALE_OF_PARENT=3;
	//另一方向的父类大小的倍率
	public static final int SCALE_OF_PARENT_OTHER=4;
	
	public static final int DP=5;

	public static final long MODE_WRAP_CONTENT=((long)WRAP_CONTENT)<<SHIFT_SIZE;
	public static final long MODE_MATCH_PARENT=((long)MATCH_PARENT)<<SHIFT_SIZE;

	public static long intToLongMode(int mode){
		return ((long)mode)<<SHIFT_SIZE;
	}
	
	public static long makeUpDP(float dp){
		return makeupParam(ViewConfiguration.dp(dp),NONE);
	}
	
	public static long makeupScaleOfParentParam(float s){
		return makeupParam(s,SCALE_OF_PARENT);
	}
	
	public static long makeupScaleOfParentOtherParam(float s){
		return makeupParam(s,SCALE_OF_PARENT_OTHER);
	}

	public static long makeupParam(float size){
		return makeupParam(size,NONE);
	}
	
	public static long makeupParam(float size,int mode){
		return intToLongMode(mode)|Float.floatToRawIntBits(size);
	}

	public static float getSize(long param){
		return Float.intBitsToFloat((int)(param&SIZE_MASK));
	}

	public static int getMode(long param){
		return (int)((param&MODE_MASK)>>>SHIFT_SIZE);
	}

	public static long adjustSize(long raw,float add){
		return makeupParam(getSize(raw)+add,getMode(raw));
	}

	public static long setSize(long raw,float size){
		return makeupParam(size,getMode(raw));
	}
}
