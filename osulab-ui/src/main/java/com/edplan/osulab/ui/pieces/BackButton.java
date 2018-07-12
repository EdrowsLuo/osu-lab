package com.edplan.osulab.ui.pieces;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.drawable.sprite.CircleSprite;
import com.edplan.framework.ui.drawable.sprite.ShadowCircleSprite;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.ui.SceneOverlay;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.framework.ui.drawable.sprite.TextureSprite;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.text.font.FontAwesome;

public class BackButton extends EdView implements Hideable
{
	private CircleSprite circle;
	
	private ShadowCircleSprite shadowCircle;
	
	private TextureSprite backSprite;
	
	private AbstractTexture backTexture;
	
	private float baseAlpha=0.7f;
	
	private float shadowDp=7;
	
	private float pressScale=0.8f;
	
	private float alpha=baseAlpha;
	
	public BackButton(MContext c){
		super(c);
		setClickable(true);
		circle=new CircleSprite(c);
		circle.setColor(UiConfig.Color.PINK,UiConfig.Color.PINK,
						UiConfig.Color.PINK,
						UiConfig.Color.PINK);
		shadowCircle=new ShadowCircleSprite(c);
		backSprite=new TextureSprite(c);
		backTexture=FontAwesome.fa_chevron_left.getTexture();
		backSprite.setTexture(backTexture);
		shadowCircle.setStartColor(Color4.rgba(0.1f,0.1f,0.1f,0.6f));
		shadowCircle.setEndColor(Color4.rgba(0,0,0,0));
	}

	@Override
	public void onInitialLayouted(){
		// TODO: Implement this method
		super.onInitialLayouted();
		directHide();
	}

	public void setAlpha(float alpha){
		this.alpha=alpha;
		circle.setAlpha(alpha*baseAlpha);
		shadowCircle.setAlpha(alpha*baseAlpha);
		backSprite.setAlpha(alpha*baseAlpha);
	}

	public float getAlpha(){
		return alpha;
	}

	public void setScale(float s){
		circle.setScale(s);
		shadowCircle.setScale(s);
		backSprite.setScale(s);
	}
	
	public void performOnPressAnimation(){
		FloatQueryAnimation anim=new FloatQueryAnimation<BackButton>(this,"scale");
		anim.transform(circle.getScaleX(),0,Easing.None);
		anim.transform(pressScale,ViewConfiguration.DEFAULT_TRANSITION_TIME/2,Easing.None);
		anim.start();
		setAnimation(anim);
	}
	
	public void performOffPressAnimation(){
		FloatQueryAnimation anim=new FloatQueryAnimation<BackButton>(this,"scale");
		anim.transform(circle.getScaleX(),0,Easing.None);
		anim.transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME/2,Easing.OutBounce);
		anim.start();
		setAnimation(anim);
	}
	
	@Override
	public void hide(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<BackButton>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<BackButton>(this,"offsetX")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(-getWidth(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		builder.together(new FloatQueryAnimation<BackButton>(this,"offsetY")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(getHeight(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					setVisiblility(VISIBILITY_GONE);
				}
			});
		anim.start();
		setAnimation(anim);
	}
	
	public void directHide(){
		setVisiblility(VISIBILITY_GONE);
		setOffsetX(-getWidth());
		setAlpha(0);
	}
	
	public void hideAndOffPress(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<BackButton>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<BackButton>(this,"offsetX")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(-getWidth(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		builder.together(new FloatQueryAnimation<BackButton>(this,"offsetY")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(getHeight(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		FloatQueryAnimation animo=new FloatQueryAnimation<BackButton>(this,"scale");
		animo.transform(circle.getScaleX(),0,Easing.None);
		animo.transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME/2,Easing.OutBounce);
		builder.together(animo,0);
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					setVisiblility(VISIBILITY_GONE);
				}
			});
		anim.start();
		setAnimation(anim);
	}

	@Override
	public void show(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<BackButton>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<BackButton>(this,"offsetX")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		builder.together(new FloatQueryAnimation<BackButton>(this,"offsetY")
						 .transform(getOffsetY(),0,Easing.None)
						 .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
		setVisiblility(VISIBILITY_SHOW);
	}

	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}
	
	@Override
	public void onStartClick(){
		// TODO: Implement this method
		super.onStartClick();
		performOnPressAnimation();
	}

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		BackQuery.get().back();
		if(BackQuery.get().remind()>0){
			performOffPressAnimation();
		}else{
			hideAndOffPress();
		}
	}

	@Override
	public void onClickEventCancel(){
		// TODO: Implement this method
		super.onClickEventCancel();
		performOffPressAnimation();
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		float r=Math.min(canvas.getWidth(),canvas.getHeight());
		circle.setArea(RectF.ltrb(0,-canvas.getHeight(),canvas.getWidth(),0));
		circle.setPosition(0,canvas.getHeight());
		circle.setRadius(r);
		
		final float shadowWidth=ViewConfiguration.dp(shadowDp);
		shadowCircle.setArea(RectF.ltrb(0,-canvas.getHeight()-shadowWidth,canvas.getWidth()+shadowWidth,0));
		shadowCircle.setPosition(0,canvas.getHeight());
		shadowCircle.setInnerRadius(r);
		shadowCircle.setRadius(r+shadowWidth);
		
		float h=canvas.getWidth()*0.45f;
		backSprite.setPosition(0,canvas.getHeight());
		backSprite.setArea(RectF.xywh(
			 canvas.getWidth()*0.15f,
			 -canvas.getHeight()*0.6f,
			 h*backTexture.getWidth()/backTexture.getHeight(),
			 h));
		shadowCircle.draw(canvas);
		circle.draw(canvas);
		backSprite.draw(canvas);
	}

	@Override
	public boolean inViewBound(float x,float y){
		// TODO: Implement this method
		float r=Math.min(getWidth(),getHeight());
		return Vec2.length(x-getLeft(),getBottom()-y)<r;
	}
}
