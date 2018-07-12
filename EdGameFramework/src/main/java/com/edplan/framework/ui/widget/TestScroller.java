package com.edplan.framework.ui.widget;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.inputs.ScrollEvent;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;

public class TestScroller extends EdView
{
	private float scroll;
	
	public TestScroller(MContext c){
		super(c);
		setClickable(true);
		setScrollableFlag(ScrollEvent.DIRECTION_VERTICAL);
	}

	@Override
	public void onClickEvent(){

		super.onClickEvent();
		//getContext().toast("on click scroller");
	}

	@Override
	public void onClickEventCancel(){

		super.onClickEventCancel();
		//getContext().toast("cancel by scroller");
	}

	@Override
	public void onStartClick(){

		super.onStartClick();
		//getContext().toast("start click by scroller");
	}
	
	@Override
	public boolean onScroll(ScrollEvent event){

		scroll+=event.getScrollY();
		setOffsetX(scroll*0.5f);
		//scroll=ScrollContainer.MAIN_SCROLL;
		return true;
	}

	@Override
	protected void onDraw(BaseCanvas canvas){

		//scroll=(ScrollContainer.MAIN_SCROLL)*getHeight()*0.999f;
		float offset=(scroll>0)?(scroll%getHeight()):(getHeight()-((-scroll)%getHeight()));
		GLPaint p=new GLPaint();
		p.setFinalAlpha(0.7f);
		p.setMixColor(Color4.rgb(0,1,0));
		canvas.drawTexture(GLTexture.White,RectF.ltrb(0,0,getWidth(),offset),p);
		p.setMixColor(Color4.rgb(0,0,1));
		canvas.drawTexture(GLTexture.White,RectF.ltrb(0,offset,getWidth(),getHeight()),p);
	}
}
