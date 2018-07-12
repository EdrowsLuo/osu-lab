package com.edplan.framework.ui.layout;
import com.edplan.framework.ui.EdView;
import android.util.Log;
import com.edplan.framework.ui.ViewConfiguration;

public class MeasureCore
{
	public static void measureChildWithMargin(
		EdView view,
		float paddingHorizon,float paddingVertical,
		long parentWidthSpec,long parentHeightSpec,
		float widthUsed,float heightUsed
	){
		final EdLayoutParam param=view.getLayoutParam();
		if(param instanceof MarginLayoutParam){
			final MarginLayoutParam mparam=(MarginLayoutParam)param;
			final long widthSpec=getChildMeasureSpec(
				parentWidthSpec,
				paddingHorizon+mparam.getMarginHorizon()+widthUsed,
				param.width,
				parentHeightSpec,
				paddingVertical);
			final long heightSpec=getChildMeasureSpec(
				parentHeightSpec,
				paddingVertical+mparam.getMarginVertical()+heightUsed,
				param.height,
				parentWidthSpec,
				paddingHorizon);
			view.measure(widthSpec,heightSpec);
		}else{
			final long widthSpec=getChildMeasureSpec(
				parentWidthSpec,
				paddingHorizon+widthUsed,
				param.width,
				parentHeightSpec,
				paddingVertical);
			final long heightSpec=getChildMeasureSpec(
				parentHeightSpec,
				paddingVertical+heightUsed,
				param.height,
				parentWidthSpec,
				paddingHorizon);
			view.measure(widthSpec,heightSpec);
		}
		
	}
	
	public static void measureChild(
		EdView view,
		float paddingHorizon,float paddingVertical,
		long parentWidthSpec,long parentHeightSpec
	){
		final EdLayoutParam param=view.getLayoutParam();
		final long widthSpec=getChildMeasureSpec(
			parentWidthSpec,
			paddingHorizon,
			param.width,
			parentHeightSpec,
			paddingVertical);
		final long heightSpec=getChildMeasureSpec(
			parentHeightSpec,
			paddingVertical,
			param.height,
			parentWidthSpec,
			paddingHorizon);
		view.measure(widthSpec,heightSpec);
		//Log.v("measure","view:"+view.getName()+" "+EdMeasureSpec.toString(widthSpec)+" : "+EdMeasureSpec.toString(heightSpec));
	}
	
	public static long getChildMeasureSpec(long spec,float padding,long childDimension,long otherSpec,float otherPadding){
		final int mode=EdMeasureSpec.getMode(spec);
		final float size=Math.max(EdMeasureSpec.getSize(spec)-padding,0);
		final int pmode=Param.getMode(childDimension);
		float rsize=0;
		int rmode=0;
		switch(mode){
			case EdMeasureSpec.MODE_DEFINEDED:
				switch(pmode){
					case Param.MATCH_PARENT:
						rsize=size;
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					case Param.WRAP_CONTENT:
						rsize=size;
						rmode=EdMeasureSpec.MODE_AT_MOST;
						break;
					case Param.SCALE_OF_PARENT_OTHER:
						rsize=Math.max(EdMeasureSpec.getSize(otherSpec)-otherPadding,0)*Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					case Param.SCALE_OF_PARENT:
						rsize=size*Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					case Param.DP:
						rsize=Param.getSize(childDimension)*ViewConfiguration.UI_UNIT;
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					default:
						rsize=Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
				}
				break;
			case EdMeasureSpec.MODE_AT_MOST:
				switch(pmode){
					case Param.MATCH_PARENT:
						rsize=size;
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					case Param.WRAP_CONTENT:
						rsize=size;
						rmode=EdMeasureSpec.MODE_AT_MOST;
						break;
					case Param.SCALE_OF_PARENT_OTHER:
						rsize=Math.max(EdMeasureSpec.getSize(otherSpec)-otherPadding,0)*Math.min(1,Param.getSize(childDimension));
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					case Param.SCALE_OF_PARENT:
						rsize=size*Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					case Param.DP:
						rsize=Param.getSize(childDimension)*ViewConfiguration.UI_UNIT;
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
					default:
						rsize=Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
				}
				break;
			case EdMeasureSpec.MODE_NONE:
			default:
				switch(pmode){
					case Param.MATCH_PARENT:
					case Param.WRAP_CONTENT:
						rsize=size;
						rmode=EdMeasureSpec.MODE_NONE;
						break;
					case Param.SCALE_OF_PARENT_OTHER:
						rsize=Math.max(EdMeasureSpec.getSize(otherSpec)-otherPadding,0)*Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_NONE;
						break;
					case Param.SCALE_OF_PARENT:
						rsize=size*Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_NONE;
						break;
					case Param.DP:
						rsize=Param.getSize(childDimension)*ViewConfiguration.UI_UNIT;
						rmode=EdMeasureSpec.MODE_NONE;
						break;
					default:
						rsize=Param.getSize(childDimension);
						rmode=EdMeasureSpec.MODE_DEFINEDED;
						break;
				}
				break;
		}
		return EdMeasureSpec.makeupMeasureSpec(rsize,rmode);
	}
}
