package com.edplan.osulab.ui;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.ScrollContainer;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.osulab.ui.MessageList;
import com.edplan.framework.ui.Anchor;

public class MessageList extends ScrollContainer implements Hideable
{
	private ColorRectSprite shadowSprite;

	public MessageList(MContext c){
		super(c);
		setOutsideTouchable(true);
		setChildoffset(ViewConfiguration.dp(1));
		setBackground(Color4.rgba(0,0,0,0.5f));
		setOrientation(Orientation.DIRECTION_T2B);
		setPaddingLeft(ViewConfiguration.dp(2));
		setPaddingRight(ViewConfiguration.dp(2));
		setGravity(Gravity.CenterLeft);

		shadowSprite=new ColorRectSprite(c);
		shadowSprite.setColor(Color4.rgba(0,0,0,0f),
							  Color4.rgba(0,0,0,0.6f),
							  Color4.rgba(0,0,0,0f),
							  Color4.rgba(0,0,0,0.6f));


		{
			TextView b=new TextView(c);
			b.setTextSize(ViewConfiguration.dp(40));
			b.setText("[Messages]");
			MarginLayoutParam param=new MarginLayoutParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.MODE_MATCH_PARENT;
			addView(b,param);
		}
	}

	@Override
	public void onInitialLayouted(){
		// TODO: Implement this method
		super.onInitialLayouted();
		directHide();
	}

	@Override
	public void hide(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<MessageList>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<MessageList>(this,"offsetX")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(getWidth(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					setVisiblility(VISIBILITY_GONE);
					BackQuery.get().unregist(MessageList.this);
				}
			});
		anim.start();
		setAnimation(anim);
	}

	public void directHide(){
		setVisiblility(VISIBILITY_GONE);
		setOffsetX(getWidth());
		setAlpha(0);
	}

	@Override
	public boolean onOutsideTouch(EdMotionEvent e){
		// TODO: Implement this method
		if(e.getEventType()==EdMotionEvent.EventType.Down){
			if(!isHidden())hide();
			return true;
		}
		return false;
	}

	@Override
	public boolean isOutsideTouchable(){
		// TODO: Implement this method
		return super.isOutsideTouchable()&&!isHidden();
	}

	@Override
	public void show(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<MessageList>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<MessageList>(this,"offsetX")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
		setVisiblility(VISIBILITY_SHOW);
		BackQuery.get().regist(this);
	}

	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}

	@Override
	public void setAlpha(float alpha){
		// TODO: Implement this method
		super.setAlpha(alpha);
		shadowSprite.setAlpha(alpha);
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		shadowSprite.setArea(RectF.anchorOWH(Anchor.TopRight,0,0,ViewConfiguration.dp(9),canvas.getHeight()));
		shadowSprite.draw(canvas);
	}
}
