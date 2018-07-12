package com.edplan.framework.ui;
import com.edplan.framework.MContext;
import android.util.TypedValue;
import android.util.Log;

public class ViewConfiguration
{
	public static int LONGCLICK_DELAY_MS=1200;
	
	public static float START_SCROLL_OFFSET=10;
	
	public static float UI_UNIT=1;
	
	public static float DEFAULT_TRANSITION_TIME=100;
	
	public static void loadContext(MContext context){
		UI_UNIT=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1, context.getNativeContext().getResources().getDisplayMetrics());
		START_SCROLL_OFFSET=UI_UNIT*5;
		Log.v("ViewConfiguration","set UI_UNIT="+UI_UNIT);
	}
	
	public static float dp(float dp){
		return UI_UNIT*dp;
	}
}
