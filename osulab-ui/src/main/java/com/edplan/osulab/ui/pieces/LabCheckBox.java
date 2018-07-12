package com.edplan.osulab.ui.pieces;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.AbstractAnimation;
import com.edplan.framework.ui.animation.AnimationHandler;
import com.edplan.framework.ui.drawable.sprite.RoundedRectSprite;
import com.edplan.framework.ui.drawable.sprite.RoundedShadowSprite;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.Anchor;

public class LabCheckBox extends EdView
{
	private float outerWidth=ViewConfiguration.dp(2);
	
	private boolean checked;
	
	private AbstractAnimation checkAnim;
	
	private RoundedShadowSprite outer;
	
	private RoundedRectSprite inner;
	
	private OnCheckListener onCheckListener;
	
	private float innerAlpha=0;
	
	private float hintAlpha=0.5f;
	
	public LabCheckBox(MContext c){
		super(c);
		setClickable(true);
		outer=new RoundedShadowSprite(c);
		outer.setAccentColor(Color4.rgba(1,1,1,1));
		outer.setColor(Color4.rgba(1,1,1,1),Color4.rgba(1,1,1,1),
					   Color4.rgba(1,1,1,1),Color4.rgba(1,1,1,1));
		outer.setShadowColor(Color4.rgba(1,1,1,1),Color4.rgba(1,1,1,1));
		
		inner=new RoundedRectSprite(c);
		inner.setAccentColor(Color4.rgba(1,1,1,1));
		inner.setColor(Color4.rgba(1,1,1,1),Color4.rgba(1,1,1,1),
					   Color4.rgba(1,1,1,1),Color4.rgba(1,1,1,1));
		
	}

	public void setInnerAlpha(float innerAlpha){
		this.innerAlpha=innerAlpha;
		invalidateDraw();
	}

	public float getInnerAlpha(){
		return innerAlpha;
	}

	public void setOnCheckListener(OnCheckListener onCheckListener){
		this.onCheckListener=onCheckListener;
	}

	public OnCheckListener getOnCheckListener(){
		return onCheckListener;
	}

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		performCheckEvent();
	}
	
	public void performCheckEvent(){
		setChecked(!isChecked());
		if(onCheckListener!=null)onCheckListener.onCheck(isChecked(),this);
	}

	public void setChecked(boolean checked){
		if(checked!=this.checked){
			this.checked=checked;
			invalidateDraw();
			if(checked){
				ComplexAnimationBuilder b=ComplexAnimationBuilder.start(new FloatQueryAnimation<LabCheckBox>(this,"innerAlpha")
												 .transform(getInnerAlpha(),0,Easing.None)
												 .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
				checkAnim=b.buildAndStart();
			}else{
				ComplexAnimationBuilder b=ComplexAnimationBuilder.start(new FloatQueryAnimation<LabCheckBox>(this,"innerAlpha")
																		.transform(getInnerAlpha(),0,Easing.None)
																		.transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
				checkAnim=b.buildAndStart();
			}
		}
	}

	public boolean isChecked(){
		return checked;
	}

	@Override
	public void performAnimation(double deltaTime){
		// TODO: Implement this method
		super.performAnimation(deltaTime);
		if(checkAnim!=null){
			if(AnimationHandler.handleSingleAnima(checkAnim,deltaTime)){
				onRemoveAnimation(checkAnim);
				checkAnim=null;
			}
		}
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		float r=Math.max(0,Math.min(canvas.getWidth(),canvas.getHeight())/2-outerWidth);
		RectF area=RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight());
		RectF iarea=area.copy().padding(outerWidth);
		outer.setArea(area);
		outer.setRect(iarea);
		outer.setShadowWidth(outerWidth);
		outer.setRoundedRadius(r);
		outer.setAlpha(hintAlpha+innerAlpha*(1-hintAlpha));
		if(checked||checkAnim!=null){
			inner.setAlpha(innerAlpha);
			inner.setArea(iarea);
			inner.setRect(iarea.copy().scale(Anchor.Center,innerAlpha,innerAlpha));
			inner.setRoundedRadius(r);
			inner.draw(canvas);
		}
		outer.draw(canvas);
	}
	
	
}
