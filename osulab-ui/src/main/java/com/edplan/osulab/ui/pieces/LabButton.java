package com.edplan.osulab.ui.pieces;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.osulab.ui.UiConfig;

public class LabButton extends RelativeContainer
{
	public static float DEFAULT_HEIGHT=ViewConfiguration.dp(17);
	
	private Color4 accentColor=UiConfig.Color.BLUE.copyNew();
	
	private RoundedLayerPoster poster;
	
	private float scale=1;
	
	private OnClickListener onClickListener;
	
	public LabButton(MContext c){
		super(c);
		setClickable(true);
		setBackground(accentColor);
		poster=new RoundedLayerPoster(c);
		poster.setAnchor(Anchor.Center);
		poster.setRoundedRadius(ViewConfiguration.dp(4));
		//poster.setShadow(ViewConfiguration.dp(2),Color4.rgba(1,1,1,0.3f),Color4.rgba(0,0,0,0));
		setLayerPoster(poster);
	}
	
	private void onPressAnim(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<LabButton>(this,"scale")
																	  .transform(getScale(),0,Easing.None)
																	  .transform(0.9f,60,Easing.InQuad));
		ComplexAnimation anim=builder.build();
		//anim.setOnProgressListener(invalideDrawDuringAnimation());
		anim.start();
		setAnimation(anim);
	}

	private void offPressAnim(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<LabButton>(this,"scale")
																	  .transform(getScale(),0,Easing.None)
																	  .transform(1,60,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		//anim.setOnProgressListener(invalideDrawDuringAnimation());
		anim.start();
		setAnimation(anim);
	}

	@Override
	public void setPressed(boolean pressed){
		// TODO: Implement this method
		super.setPressed(pressed);
		if(pressed){
			onPressAnim();
		}else{
			offPressAnim();
		}
	}

	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener=onClickListener;
	}

	public OnClickListener getOnClickListener(){
		return onClickListener;
	}

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		if(onClickListener!=null)onClickListener.onClick(this);
	}

	public void setScale(float scale){
		this.scale=scale;
		poster.setScale(scale);
		invalidateDraw();
	}

	public float getScale(){
		return scale;
	}
	
	public void setRoundedRadius(float r){
		poster.setRoundedRadius(r);
	}
	
	public float getRoundedRadius(){
		return poster.getRoundedRadius();
	}
}
