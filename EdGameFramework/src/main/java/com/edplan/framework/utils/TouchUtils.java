package com.edplan.framework.utils;
import android.view.MotionEvent;

public class TouchUtils
{
	
	public static int indexOfId(int id,MotionEvent event){
		for(int i=0;i<event.getPointerCount();i++){
			if(event.getPointerId(i)==id){
				return i;
			}
		}
		return -1;
	}
	
}
