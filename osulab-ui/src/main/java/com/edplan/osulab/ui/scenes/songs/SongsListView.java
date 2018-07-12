package com.edplan.osulab.ui.scenes.songs;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.interfaces.Setter;
import com.edplan.framework.math.FMath;
import com.edplan.framework.ui.EdContainer;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.drawable.RoundedRectDrawable;
import com.edplan.framework.ui.inputs.ScrollEvent;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.MeasureCore;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.AbsoluteContainer;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.framework.ui.widget.component.Scroller;
import com.edplan.osulab.ui.pieces.SongPanel;
import com.edplan.framework.ui.widget.RelativeContainer;
import java.util.ArrayList;
import java.util.LinkedList;

public class SongsListView extends EdContainer implements Hideable
{
	public static float WIDTH_DP=350;
	
	protected float childoffset=ViewConfiguration.dp(5);

	protected float startOffset;

	protected float scrollRate;

	protected float maxChildrenSize=500;

	protected boolean enableGravityCenter=false;
	
	private int startIndex=0;
	
	private int showItemCount=0;
	
	private ArrayList<EdView> contents=new ArrayList<EdView>();
	
	private LinkedList<EntryContainer> showContent=new LinkedList<EntryContainer>();
	
	public SongsListView(MContext c){
		super(c);
		setScrollableFlag(ScrollEvent.DIRECTION_VERTICAL);
		//setDebug(true);
		
		scroller.setEnableOverscroll(false);
		
		
		for(int i=0;i<20;i++){
			MarginLayoutParam p=new MarginLayoutParam();
			RelativeContainer view=new RelativeContainer(c);
			
			RoundedRectDrawable bg=new RoundedRectDrawable(c);
			bg.setColor(Color4.rgba(0,0,0,0.5f));
			bg.setRadius(ViewConfiguration.dp(10));
			view.setBackground(bg);
			
			p.width=Param.makeUpDP(WIDTH_DP);
			p.height=Param.makeUpDP(WIDTH_DP*0.15f);
			view.setLayoutParam(p);
			contents.add(view);
			/*
			addView(view,p);
			*/
		}
	}

	protected Scroller scroller=new Scroller(new Setter<Float>(){
			@Override
			public void set(Float t){
				// TODO: Implement this method
				scrollRate=t;
				invalidate();
				invalidateDraw();
			}
		});

	@Override
	public boolean onScroll(ScrollEvent event){
		// TODO: Implement this method
		final float offset=-event.getScrollY();
		switch(event.getState()){
			case ScrollEvent.STATE_START:
				scroller.setStartValue(getScrollRateMin());
				scroller.setEndValue(getScrollRateMax());
				scroller.start(scrollRate);
				scroller.update();
				return true;
			case ScrollEvent.STATE_SCROLLING:
				scroller.add(offset,event.getDeltaTime());
				scroller.update();
				return true;
			case ScrollEvent.STATE_END:
				scroller.end();
				scroller.update();
				return true;
			case ScrollEvent.STATE_CANCEL:
				scroller.cancel();
				scroller.update();
				return true;
		}
		return false;
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

	public void setChildoffset(float childoffset){
		this.childoffset=childoffset;
	}

	public float getChildoffset(){
		return childoffset;
	}
	
	@Override
	public void hide(){
		// TODO: Implement this method
		ComplexAnimationBuilder b=ComplexAnimationBuilder.
		//start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		start(new FloatQueryAnimation<EdView>(this,"offsetX")
				   .transform(0,0,Easing.None)
				   .transform(ViewConfiguration.dp(WIDTH_DP),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}

	@Override
	public void show(){
		// TODO: Implement this method
		setAlpha(0);
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		b.together(new FloatQueryAnimation<EdView>(this,"offsetX")
				   .transform(ViewConfiguration.dp(WIDTH_DP),0,Easing.None)
				   .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME*2,Easing.OutQuad));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}
	

	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		
	}
	
	

	protected float getScrollRateMax(){
		return getHeight()/2;
	}

	protected float getScrollRateMin(){
		return -getHeight()/2;
	}

	@Override
	protected void drawContainer(BaseCanvas canvas){
		// TODO: Implement this method
		final int count=getChildrenCount();
		for(int i=0;i<count;i++){
			final EdView view=getChildAt(i);
			if(view.getBottom()<0)continue;
			if(view.getTop()>getHeight())break;
			if(view.getVisiblility()==VISIBILITY_SHOW){
				final int savedcount=canvas.save();
				try{
					canvas.translate(view.getLeft(),view.getTop());
					canvas.clip(view.getWidth(),view.getHeight());
					view.draw(canvas);
				}finally{
					canvas.restoreToCount(savedcount);
				}
			}
		}
	}

	@Override
	public EdLayoutParam adjustParam(EdView view,EdLayoutParam param){
		// TODO: Implement this method
		if(param instanceof MarginLayoutParam){
			return param;
		}else{
			return new MarginLayoutParam(param);
		}
	}

	private boolean hasPreviousEntry(EntryContainer entry){
		return false;
	}


	private boolean hasNextEntry(EntryContainer entry){
		return false;
	}

	private EntryContainer previousEntry(EntryContainer entry){
		return null;
	}

	private EntryContainer nextEntry(EntryContainer entry){
		return null;
	}

	private EntryContainer gainEntryAtStart(){
		EntryContainer e=showContent.getFirst();
		if(e==null){
			return null;
		}

		if(e.getTop()<0)return null;
		EntryContainer p=previousEntry(e);
		if(p==null)return null;

		showContent.addFirst(p);
		return  p;
	}

	private EntryContainer gainEntryAtEnd(){
		return null;
	}

	private boolean isInitialLayout=true;


	protected void layoutVertical(float left,float top,float right,float bottom){
		final int count=getChildrenCount();

		final float parentTop=getPaddingTop();
		final float parentRight=right-left-getPaddingRight();







		/*
		
		float heightUsed=parentTop+startOffset-scrollRate;
		
		for(int i=0;i<count;i++){
			final EdView view=getChildAt(i);
			if(view.getVisiblility()!=VISIBILITY_GONE){
				final MarginLayoutParam param=(MarginLayoutParam)view.getLayoutParam();
				
				final float dy=param.yoffset+heightUsed+param.marginTop;
				final float yp=(dy+view.getMeasuredHeight()/2)/(bottom-top);
				final float dx=(1-FMath.sin(yp*FMath.Pi)*0.9f)*(right-left)*0.3f;
				
				//FMath.sin(yp*FMath.Pi)*(right-left)*0.3f*0+param.xoffset+parentRight-param.marginRight;
				
				view.layout(dx,dy,view.getMeasuredWidth()+dx,view.getMeasuredHeight()+dy);
				heightUsed+=param.getMarginVertical()+view.getMeasuredHeight()+childoffset;
			}
		}

		*/
	}

	@Override
	protected void onLayout(boolean changed,float left,float top,float right,float bottom){
		// TODO: Implement this method
		scroller.update();
		layoutVertical(left,top,right,bottom);
	}

	private void measureEntry(EntryContainer view){
		final EdLayoutParam param=view.getLayoutParam();
		final float marginVertical=(param instanceof MarginLayoutParam)?(((MarginLayoutParam)param).getMarginVertical()):0;
		measureChildWithMarginIgnorePadding(view, EdMeasureSpec.makeupMeasureSpec(getWidth(),EdMeasureSpec.MODE_DEFINEDED),EdMeasureSpec.makeupMeasureSpec(getWidth(),EdMeasureSpec.MODE_NONE),getPaddingHorizon(),0);
	}

	protected void measureVertical(long widthSpec,long heightSpec){
		//对内容的measure在layout的时候进行
		/*
		final int count=getChildrenCount();
		final long adjustedSpec=EdMeasureSpec.makeupMeasureSpec(Math.max(EdMeasureSpec.getSize(heightSpec),maxChildrenSize),EdMeasureSpec.MODE_AT_MOST);
		for(int i=0;i<count;i++){
			final EdView view=getChildAt(i);
			final EdLayoutParam param=view.getLayoutParam();
			final float marginVertical=(param instanceof MarginLayoutParam)?(((MarginLayoutParam)param).getMarginVertical()):0;
			if(view.getVisiblility()!=VISIBILITY_GONE){
				measureChildWithMarginIgnorePadding(view,widthSpec,adjustedSpec,getPaddingHorizon(),0);
			}
		}
		*/
		float xd=EdMeasureSpec.getSize(widthSpec);;
		float yd;
		/*
		{
			final int mmode=EdMeasureSpec.getMode(widthSpec);
			switch(mmode){
				case EdMeasureSpec.MODE_DEFINEDED:
					xd=EdMeasureSpec.getSize(widthSpec);
					break;
				case EdMeasureSpec.MODE_NONE:
				default:
					xd=getPaddingHorizon()+getDefaultMaxChildrenMeasuredWidthWithMargin();
					break;
			}
		}
		*/
		{
			final int mmode=EdMeasureSpec.getMode(heightSpec);
			switch(mmode){
				case EdMeasureSpec.MODE_DEFINEDED:
					yd=EdMeasureSpec.getSize(heightSpec);
					break;
				case EdMeasureSpec.MODE_NONE:
				default:
					yd=Math.max(getPaddingHorizon(),EdMeasureSpec.getSize(heightSpec));
					break;
			}
		}
		//Log.v("layout","measure ScrollContainer Vertical, height used "+heightUsed+", set dimensition "+xd+";"+yd+" spec:"+EdMeasureSpec.toString(widthSpec)+":"+EdMeasureSpec.toString(heightSpec));
		setMeasuredDimensition(xd,yd);
	}

	@Override
	protected void onMeasure(long widthSpec,long heightSpec){
		// TODO: Implement this method
		measureVertical(widthSpec,heightSpec);
	}
	
	
	public class EntryContainer extends AbsoluteContainer{

		private Object adaptedObject;

		private int adaptedIndex;

		private boolean isSelectedItem=false;

		private EdView content;

		public EntryContainer(MContext c){
			super(c);
		}

		public Object getAdaptedObject() {
			return adaptedObject;
		}

		public void setAdaptedObject(Object adaptedObject) {
			this.adaptedObject = adaptedObject;
		}
		@Override
		public int getChildrenCount() {
			return content==null?0:1;
		}

		@Override
		public EdView getChildAt(int i) {
			if(i==0)return content;
			return null;
		}

		public EdView getContent() {
			return content;
		}

		public void setContent(EdView content) {
			this.content = content;
		}

		public int getAdaptedIndex() {
			return adaptedIndex;
		}

		public void setAdaptedIndex(int adaptedIndex) {
			this.adaptedIndex = adaptedIndex;
		}
	}
}
