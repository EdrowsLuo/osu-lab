package com.edplan.framework.ui.widget;
import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.utils.BitUtil;
import com.edplan.framework.ui.EdContainer;
import android.util.Log;

public class LinearLayout extends EdAbstractViewGroup
{
	protected int orientation;
	
	protected float childoffset;
	
	protected float startOffset;
	
	protected boolean enableGravityCenter=true;
	
	public LinearLayout(MContext c){
		super(c);
		orientation=Orientation.DIRECTION_L2R;
	}

	public void setEnableGravityCenter(boolean enableGravityCenter){
		this.enableGravityCenter=enableGravityCenter;
	}

	public boolean isEnableGravityCenter(){
		return enableGravityCenter;
	}

	public void setStartOffset(float startOffset){
		this.startOffset=startOffset;
	}

	public float getStartOffset(){
		return startOffset;
	}

	public float getLatestUsedSpace(){
		return latestUsedSpace;
	}

	public void setChildoffset(float childoffset){
		this.childoffset=childoffset;
	}

	public float getChildoffset(){
		return childoffset;
	}

	public void setOrientation(int orientation){
		this.orientation=orientation;
	}

	public int getOrientation(){
		return orientation;
	}

	@Override
	public EdLayoutParam adjustParam(EdView view,EdLayoutParam param){

		if(param instanceof MarginLayoutParam){
			return param;
		}else{
			return new MarginLayoutParam(param);
		}
	}
	
	private float latestUsedSpace;
	
	protected void layoutHorizon(float left,float top,float right,float bottom){
		final int count=getChildrenCount();

		final float parentLeft=getPaddingLeft();
		final float parentTop=getPaddingTop();
		final float parentBottom=bottom-top-getPaddingBottom();
		final float parentRight=right-left-getPaddingRight();
		float widthUsed=parentLeft+startOffset;
		final int gravity=getGravity();
		if(enableGravityCenter)if(gravity==Gravity.Center){
			final float cy=(parentTop+parentBottom)/2;
			widthUsed=(parentLeft+parentRight-latestUsedSpace)/2;
			for(int i=0;i<count;i++){
				final EdView view=getChildAt(i);
				if(view.getVisiblility()!=VISIBILITY_GONE){
					final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
					final float dx=param.xoffset+widthUsed+param.marginLeft;
					final float dy=param.yoffset+cy-view.getMeasuredHeight()/2+(param.marginTop-param.marginBottom)/2;
					view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
					widthUsed+=param.getMarginHorizon()+view.getMeasuredWidth()+childoffset;
				}
			}
			return;
		}
		switch(gravity&Gravity.MASK_VERTICAL){
			case Gravity.BOTTOM:{
					for(int i=0;i<count;i++){
						final EdView view=getChildAt(i);
						if(view.getVisiblility()!=VISIBILITY_GONE){
							final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
							final float dx=param.xoffset+widthUsed+param.marginLeft;
							final float dy=param.yoffset+parentBottom-param.marginBottom;
							view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
							widthUsed+=param.getMarginHorizon()+view.getMeasuredWidth()+childoffset;
						}
					}
				}break;
			case Gravity.CENTER_VERTICAL:{
					final float cy=(parentTop+parentBottom)/2;
					for(int i=0;i<count;i++){
						final EdView view=getChildAt(i);
						if(view.getVisiblility()!=VISIBILITY_GONE){
							final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
							final float dx=param.xoffset+widthUsed+param.marginLeft;
							final float dy=param.yoffset+cy-view.getMeasuredHeight()/2;
							view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
							widthUsed+=param.getMarginHorizon()+view.getMeasuredWidth()+childoffset;
						}
					}
				}break;
			case Gravity.TOP:
			default:{
					for(int i=0;i<count;i++){
						final EdView view=getChildAt(i);
						if(view.getVisiblility()!=VISIBILITY_GONE){
							final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
							final float dx=param.xoffset+widthUsed+param.marginLeft;
							final float dy=param.yoffset+parentTop+param.marginTop;
							view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
							widthUsed+=param.getMarginHorizon()+view.getMeasuredWidth()+childoffset;
						}
					}
				}break;
		}
	}
	
	protected void layoutVertical(float left,float top,float right,float bottom){
		final int count=getChildrenCount();

		final float parentLeft=getPaddingLeft();
		final float parentTop=getPaddingTop();
		final float parentBottom=bottom-top-getPaddingBottom();
		final float parentRight=right-left-getPaddingRight();
		float heightUsed=parentTop+startOffset;
		final int gravity=getGravity();
		if(enableGravityCenter)if(gravity==Gravity.Center){
			final float cx=(parentLeft+parentRight)/2;
			heightUsed=(parentTop+parentBottom-latestUsedSpace)/2;
			for(int i=0;i<count;i++){
				final EdView view=getChildAt(i);
				if(view.getVisiblility()!=VISIBILITY_GONE){
					final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
					final float dx=param.xoffset+cx-view.getMeasuredWidth()/2;
					final float dy=param.yoffset+heightUsed+param.marginTop;
					view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
					heightUsed+=param.getMarginVertical()+view.getMeasuredHeight()+childoffset;
				}
			}
			return;
		}
		switch(gravity&Gravity.MASK_HORIZON){
			case Gravity.RIGHT:{
					for(int i=0;i<count;i++){
						final EdView view=getChildAt(i);
						if(view.getVisiblility()!=VISIBILITY_GONE){
							final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
							final float dx=param.xoffset+parentRight-param.marginRight;
							final float dy=param.yoffset+heightUsed+param.marginTop;
							view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
							heightUsed+=param.getMarginVertical()+view.getMeasuredHeight()+childoffset;
						}
					}
				}break;
			case Gravity.CENTER_HORIZON:{
					final float cx=(parentLeft+parentRight)/2;
					for(int i=0;i<count;i++){
						final EdView view=getChildAt(i);
						if(view.getVisiblility()!=VISIBILITY_GONE){
							final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
							final float dx=param.xoffset+cx-view.getMeasuredWidth()/2;
							final float dy=param.yoffset+heightUsed+param.marginTop;
							view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
							heightUsed+=param.getMarginVertical()+view.getMeasuredHeight()+childoffset;
						}
					}
				}break;
			case Gravity.LEFT:
			default:{
					for(int i=0;i<count;i++){
						final EdView view=getChildAt(i);
						if(view.getVisiblility()!=VISIBILITY_GONE){
							final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
							final float dx=param.xoffset+parentLeft+param.marginLeft;
							final float dy=param.yoffset+heightUsed+param.marginTop;
							view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
							heightUsed+=param.getMarginVertical()+view.getMeasuredHeight()+childoffset;
						}
					}
				}break;
		}
	}
	
	@Override
	protected void onLayout(boolean changed,float left,float top,float right,float bottom){

		if(Orientation.getMainOrientation(orientation)==Orientation.DIRECTION_VERTICAL){
			layoutVertical(left,top,right,bottom);
		}else{
			layoutHorizon(left,top,right,bottom);
		}
	}
	
	protected void measureHorizon(long widthSpec,long heightSpec){
		final int count=getChildrenCount();
		float widthUsed=0;
		for(int i=0;i<count;i++){
			final EdView view=getChildAt(i);
			final EdLayoutParam param=view.getLayoutParam();
			final float marginHorizon=(param instanceof MarginLayoutParam)?(((MarginLayoutParam)param).getMarginHorizon()):0;
			if(view.getVisiblility()!=VISIBILITY_GONE){
				measureChildWithMargin(view,widthSpec,heightSpec,widthUsed,0);
				widthUsed+=view.getMeasuredWidth()+marginHorizon;
				if(i!=count-1)widthUsed+=childoffset;
			}
		}
		latestUsedSpace=widthUsed;
		float xd;
		float yd;
		{
			final int mmode=EdMeasureSpec.getMode(widthSpec);
			switch(mmode){
				case EdMeasureSpec.MODE_DEFINEDED:
					xd=EdMeasureSpec.getSize(widthSpec);
					break;
				case EdMeasureSpec.MODE_AT_MOST:
					xd=Math.min(EdMeasureSpec.getSize(widthSpec),getPaddingHorizon()+widthUsed);
					break;
				case EdMeasureSpec.MODE_NONE:
				default:
					xd=getPaddingHorizon()+widthUsed;
					break;
			}
		}
		{
			final int mmode=EdMeasureSpec.getMode(heightSpec);
			switch(mmode){
				case EdMeasureSpec.MODE_DEFINEDED:
					yd=EdMeasureSpec.getSize(heightSpec);
					break;
				case EdMeasureSpec.MODE_AT_MOST:
					yd=Math.min(EdMeasureSpec.getSize(heightSpec),getPaddingVertical()+getDefaultMaxChildrenMeasuredHeightWithMargin());
					break;
				case EdMeasureSpec.MODE_NONE:
				default:
					yd=getPaddingHorizon()+getDefaultMaxChildrenMeasuredHeightWithMargin();
					break;
			}
		}
		//Log.v("layout","measure LinearLayout Horizon, width used "+widthUsed+", set dimensition "+xd+";"+yd+" spec:"+EdMeasureSpec.toString(widthSpec)+":"+EdMeasureSpec.toString(heightSpec));
		setMeasuredDimensition(xd,yd);
	}
	
	protected void measureVertical(long widthSpec,long heightSpec){
		final int count=getChildrenCount();
		float heightUsed=0;
		for(int i=0;i<count;i++){
			final EdView view=getChildAt(i);
			final EdLayoutParam param=view.getLayoutParam();
			final float marginVertical=(param instanceof MarginLayoutParam)?(((MarginLayoutParam)param).getMarginVertical()):0;
			if(view.getVisiblility()!=VISIBILITY_GONE){
				measureChildWithMargin(view,widthSpec,heightSpec,0,heightUsed);
				heightUsed+=view.getMeasuredHeight()+marginVertical;
				if(i!=count-1)heightUsed+=childoffset;
			}
		}
		latestUsedSpace=heightUsed;
		float xd;
		float yd;
		{
			final int mmode=EdMeasureSpec.getMode(widthSpec);
			switch(mmode){
				case EdMeasureSpec.MODE_DEFINEDED:
					xd=EdMeasureSpec.getSize(widthSpec);
					break;
				case EdMeasureSpec.MODE_AT_MOST:
					xd=Math.min(EdMeasureSpec.getSize(widthSpec),getPaddingHorizon()+getDefaultMaxChildrenMeasuredWidthWithMargin());
					break;
				case EdMeasureSpec.MODE_NONE:
				default:
					xd=getPaddingHorizon()+getDefaultMaxChildrenMeasuredWidthWithMargin();
					break;
			}
		}
		{
			final int mmode=EdMeasureSpec.getMode(heightSpec);
			switch(mmode){
				case EdMeasureSpec.MODE_DEFINEDED:
					yd=EdMeasureSpec.getSize(heightSpec);
					break;
				case EdMeasureSpec.MODE_AT_MOST:
					yd=Math.min(EdMeasureSpec.getSize(heightSpec),getPaddingVertical()+heightUsed);
					break;
				case EdMeasureSpec.MODE_NONE:
				default:
					yd=getPaddingHorizon()+heightUsed;
					break;
			}
		}
		//Log.v("layout","measure LinearLayout Vertical, height used "+heightUsed+", set dimensition "+xd+";"+yd+" spec:"+EdMeasureSpec.toString(widthSpec)+":"+EdMeasureSpec.toString(heightSpec));
		setMeasuredDimensition(xd,yd);
	}

	@Override
	protected void onMeasure(long widthSpec,long heightSpec){

		if(Orientation.getMainOrientation(orientation)==Orientation.DIRECTION_VERTICAL){
			measureVertical(widthSpec,heightSpec);
		}else{
			measureHorizon(widthSpec,heightSpec);
		}
	}
}
