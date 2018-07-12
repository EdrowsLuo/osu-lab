package com.edplan.osulab.ui.scenes;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.AbstractAnimation;
import com.edplan.framework.ui.animation.AnimationHandler;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.widget.LinearContainer;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.widget.TextureView;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.framework.ui.widget.LinearLayout;

public class SceneSelectButton extends LinearLayout implements Hideable
{
	public static double ANIM_DURATION=ViewConfiguration.DEFAULT_TRANSITION_TIME*0.7;
	
	private ColorRectSprite bg;
	
	private ColorRectSprite shadow;
	
	private float hideX;
	
	private TextView textView;
	
	private TextureView textureView;
	
	private boolean leftShadow=false;
	
	private Color4 accentColor=Color4.rgba(0,0,0,0);
	
	private OnClickListener onClickListener;
	
	private float widthAddition=0;
	
	private float maxWidthAddition=ViewConfiguration.dp(25);
	
	private float showOffset=ViewConfiguration.dp(300);
	
	private AbstractAnimation pressAnim;
	
	public SceneSelectButton(MContext c){
		super(c);
		bg=new ColorRectSprite(c);
		accentColor.set(UiConfig.Color.BLUE_DARK);
		bg.setAccentColor(UiConfig.Color.BLUE_DARK);
		bg.setColor(Color4.gray(0.9f),Color4.gray(1),
					Color4.gray(0.9f),Color4.gray(1));
		shadow=new ColorRectSprite(c);
		float gr=0.2f;
		shadow.setColor(Color4.rgba(gr,gr,gr,0.4f),
						Color4.rgba(0,0,0,0f),
						Color4.rgba(gr,gr,gr,0.4f),
						Color4.rgba(0,0,0,0f));
		setOrientation(Orientation.DIRECTION_T2B);
		setGravity(Gravity.Center);
		setClickable(true);
		{
			textureView=new TextureView(c);
			textureView.setBlendType(BlendType.Additive);
			MarginLayoutParam p=new MarginLayoutParam();
			p.width=Param.makeUpDP(22);
			p.height=Param.makeUpDP(22);
			p.marginTop=ViewConfiguration.dp(8);
			p.marginBottom=ViewConfiguration.dp(4);
			addView(textureView,p);
		}
		
		{
			textView=new TextView(c);
			textView.setFont(BMFont.Exo_20_Semi_Bold);
			textView.setTextSize(ViewConfiguration.dp(12));
			MarginLayoutParam p=new MarginLayoutParam();
			p.width=Param.MODE_WRAP_CONTENT;
			p.height=Param.MODE_WRAP_CONTENT;
			addView(textView,p);
		}
		
	}

	public void setWidthAddition(float widthAddition){
		this.widthAddition=widthAddition;
		invalidateDraw();
	}

	public float getWidthAddition(){
		return widthAddition;
	}

	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener=onClickListener;
	}

	public OnClickListener getOnClickListener(){
		return onClickListener;
	}
	
	public void setLeftShadow(){
		leftShadow=true;
		float gr=0.2f;
		shadow.setColor(Color4.rgba(0,0,0,0f),
						Color4.rgba(gr,gr,gr,0.4f),
						Color4.rgba(0,0,0,0f),
						Color4.rgba(gr,gr,gr,0.4f));
	}
	
	public void setTexture(AbstractTexture t){
		textureView.setTexture(t);
	}
	
	public void setText(String text){
		textView.setText(text);
	}

	/*
	@Override
	public void setAlpha(float alpha){
		// TODO: Implement this method
		super.setAlpha(alpha);
		bg.setAlpha(alpha);
	}
	*/
	
	public void setHideX(float hideX){
		this.hideX=hideX;
	}

	public float getHideX(){
		return hideX;
	}
	
	public void setBackgroundColor(Color4 c){
		accentColor.set(c);
	}
	
	public void onPressAnim(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButton>(this,"widthAddition")
																	  .transform(getWidthAddition(),0,Easing.None)
																	  .transform(maxWidthAddition,100,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		anim.start();
		pressAnim=anim;
	}

	public void offPressAnim(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButton>(this,"widthAddition")
																	  .transform(getWidthAddition(),0,Easing.None)
																	  .transform(0,100,Easing.None));
		ComplexAnimation anim=builder.build();
		anim.start();
		pressAnim=anim;
	}

	@Override
	public void performAnimation(double deltaTime){
		// TODO: Implement this method
		if(pressAnim!=null){
			if(AnimationHandler.handleSingleAnima(pressAnim,deltaTime)){
				pressAnim=null;
			}
		}
		super.performAnimation(deltaTime);
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

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		if(onClickListener!=null)onClickListener.onClick(this);
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		
		//widthAddition=isPressed()?maxWidthAddition:0;
		
		if(leftShadow){
			shadow.setParallelogram(RectF.anchorOWH(Anchor.TopRight,-widthAddition,0,ViewConfiguration.dp(8),canvas.getHeight()),UiConfig.PARALLELOGRAM_ANGEL);
			bg.setParallelogram(RectF.anchorOWH(Anchor.TopRight,canvas.getWidth(),0,canvas.getWidth()+widthAddition,canvas.getHeight()),UiConfig.PARALLELOGRAM_ANGEL);
		}else{
			shadow.setParallelogram(RectF.anchorOWH(Anchor.TopLeft,canvas.getWidth()+widthAddition,0,ViewConfiguration.dp(8),canvas.getHeight()),UiConfig.PARALLELOGRAM_ANGEL);
			bg.setParallelogram(RectF.anchorOWH(Anchor.TopLeft,0,0,canvas.getWidth()+widthAddition,canvas.getHeight()),UiConfig.PARALLELOGRAM_ANGEL);
		}
		shadow.draw(canvas);
		
		
		bg.setAccentColor(isPressed()?accentColor.copyNew().setAlpha(1.3f):accentColor);
		bg.draw(canvas);
		
		super.onDraw(canvas);
	}
	
	@Override
	public void hide(){
		// TODO: Implement this method
		showOffset=getParent().getWidth()+getWidth();
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButton>(this,"offsetX")
																	  .transform(getOffsetX(),0,Easing.None)
																	  .transform(leftShadow?-showOffset:showOffset,ANIM_DURATION,Easing.OutExpo));
		/*builder.together(new FloatQueryAnimation<SceneSelectButton>(this,"alpha")
						 .transform(getAlpha(),0,Easing.None)
						 .transform(0,ANIM_DURATION,Easing.None));*/
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
	}

	@Override
	public void show(){
		// TODO: Implement this method
		showOffset=getParent().getWidth()+getWidth();
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButton>(this,"offsetX")
																	  .transform(leftShadow?-showOffset:showOffset,0,Easing.None)
																	  .transform(0,ANIM_DURATION,Easing.OutExpo));
		/*builder.together(new FloatQueryAnimation<SceneSelectButton>(this,"alpha")
						 .transform(0,0,Easing.None)
						 .transform(1,ANIM_DURATION,Easing.None));*/
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
		
	}

	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}
}
