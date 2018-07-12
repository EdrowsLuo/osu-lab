package com.edplan.osulab.ui.pieces;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.interfaces.FloatInvokeSetter;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Quad;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.EdContainer;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.AbstractAnimation;
import com.edplan.framework.ui.animation.AnimationHandler;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.drawable.sprite.CircleSprite;
import com.edplan.framework.ui.drawable.sprite.ShadowCircleSprite;
import com.edplan.framework.ui.drawable.sprite.TextureCircleSprite;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.osulab.ui.popup.RenderStatePopupView;
import com.edplan.osulab.ui.scenes.BaseScene;
import com.edplan.osulab.ui.scenes.SceneSelectButtonBar;
import java.io.IOException;
import java.util.Random;

public class JumpingCircle extends EdView
{
	private float maxShadowWidth=12;
	
	private float shadowWidth=maxShadowWidth;
	
	private CircleSprite ring;
	private CircleSprite pinkCover;
	private ShadowCircleSprite shadow,shadowInner;
	private TextureCircleSprite centerRing;
	
	private GLTexture logo;
	
	private BoundOverlay boundOverlay;
	private BaseBoundOverlay initialBound;
	
	private int ringCount=0;
	private PartRing[] rings;
	private float minSpeed=FMath.Pi2/40000;
	private float maxSpeed=FMath.Pi2/13000;
	private float maxAngle=FMath.Pi*0.6f;
	private float minAngle=FMath.Pi/4;
	private int steps=20;
	private int radiusSteps=3;
	private float ringRadius=0.9f/steps*radiusSteps;
	private Color4[] colors={
		Color4.rgb255(255,125,183),
		Color4.rgb255(245,115,173),
		Color4.rgb255(251,121,179),
		Color4.rgb255(253,123,181),
		Color4.rgb255(230,100,158),
		Color4.rgb255(227,96,154),
		Color4.rgb255(253,123,181),
		UiConfig.Color.PINK
	};
	
	private boolean performingAnim=true;
	
	private AbstractAnimation boundAnim;
	
	public JumpingCircle(MContext c){
		super(c);
		
		rings=new PartRing[ringCount];
		Random r=new Random();
		for(int i=0;i<rings.length;i++){
			final Color4 color=colors[r.nextInt(colors.length)];
			final float innerRadius=0.9f*r.nextInt(steps)/steps;
			final float radius=Math.min(0.9f,innerRadius+ringRadius*((r.nextInt(radiusSteps)+1)/(float)radiusSteps));
			final float angle=FMath.linear(r.nextFloat(),minAngle,maxAngle);
			final float speed=(r.nextBoolean()?1:-1)*FMath.linear(r.nextFloat(),minSpeed,maxSpeed);
			rings[i]=new PartRing(angle,speed,innerRadius,radius,FMath.Pi2*r.nextFloat(),color);
		}
		
		for(PartRing rr:rings){
			rr.sprite.setScale(0);
		}
		
		
		ring=new CircleSprite(c);
		centerRing=new TextureCircleSprite(c);
		
		shadow=new ShadowCircleSprite(c);
		float gr=0.8f;
		shadow.setStartColor(Color4.rgba(gr,gr,gr,0.7f));
		shadow.setEndColor(Color4.rgba(gr,gr,gr,0));
		shadow.setBlendType(BlendType.Additive);
		
		shadowInner=new ShadowCircleSprite(c);
		shadowInner.setEndColor(Color4.rgba(1,1,1,0.3f));
		shadowInner.setStartColor(Color4.rgba(1,1,1,0));
		shadowInner.setInner();
		shadowInner.setBlendType(BlendType.Additive);
		
		pinkCover=new CircleSprite(c);
		pinkCover.setAccentColor(UiConfig.Color.PINK);
		/*
		pinkCover.setColor(Color4.rgba(1,1,1,1f),
						   Color4.rgba(1,1,1,1f),
						   Color4.gray(0.9f),
						   Color4.gray(0.9f));*/
		try{
			logo=getContext().getAssetResource().loadTexture("osu/ui/logo.png");
			centerRing.setTexture(logo,null);
		}catch(IOException e){
			e.printStackTrace();
			getContext().toast("err");
		}
	}

	@Override
	public void performAnimation(double deltaTime){
		// TODO: Implement this method
		super.performAnimation(deltaTime);
		if(boundAnim!=null){
			if(AnimationHandler.handleSingleAnima(boundAnim,deltaTime)){
				boundAnim=null;
			}
		}
	}
	
	@Override
	public void onInitialLayouted(){
		// TODO: Implement this method
		super.onInitialLayouted();
		BaseBoundOverlay b=new BaseBoundOverlay();
		initialBound=b;
		b.setLeft(getLeft());
		b.setTop(getTop());
		b.setRight(getRight());
		b.setBottom(getBottom());
		boundOverlay=b;
	}
	
	public void exitAnimation(){
		setClickable(false);
		performingAnim=true;
		final float radius=getWidth()/2;

		ring.setPosition(getWidth()/2,getHeight()/2);
		ring.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
		pinkCover.setPosition(getWidth()/2,getHeight()/2);
		pinkCover.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
		centerRing.setPosition(getWidth()/2,getHeight()/2);
		centerRing.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));

		float shadowWidthPx=ViewConfiguration.dp(maxShadowWidth);
		shadow.setPosition(getWidth()/2,getHeight()/2);
		shadow.setArea(RectF.ltrb(-getWidth()/2-shadowWidthPx,-getHeight()/2-shadowWidthPx,getWidth()/2+shadowWidthPx,getHeight()/2+shadowWidthPx));

		shadowInner.setPosition(getWidth()/2,getHeight()/2);
		shadowInner.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));

		pinkCover.setAccentColor(UiConfig.Color.PINK);
		pinkCover.setAlpha(1);

		FloatQueryAnimation anim=new FloatQueryAnimation<JumpingCircle>(this,"radius");
		anim.transform(radius,0,Easing.None);
		anim.transform(0,500,Easing.InQuad);
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(anim);
		{
			FloatQueryAnimation anim2=new FloatQueryAnimation<JumpingCircle>(JumpingCircle.this,"inner");
			anim2.transform(radius*0.9f,0,Easing.None);
			anim2.transform(0,500,Easing.InQuad);
			builder.together(anim2,0);
		}

		
		builder.together(new FloatQueryAnimation<JumpingCircle>(this,new FloatInvokeSetter<JumpingCircle>(){
								 @Override
								 public void invoke(JumpingCircle target,float v){
									 // TODO: Implement this method
									 for(PartRing r:rings){
										 r.sprite.setScale(v);
									 }
								 }
							 })
						 .transform(1,0,Easing.None)
						 .transform(0,500,Easing.None)
						 );
		
		ComplexAnimation camin=builder.build();
		camin.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					System.exit(0);
				}
			});
		camin.start();
		ComplexAnimationBuilder builder2=ComplexAnimationBuilder.start(
			FloatQueryAnimation.fadeTo(getContext().getViewRoot().getRootContainer(),0,500,Easing.OutQuad));
		getContext().getViewRoot().getRootContainer().setAnimation(builder2.buildAndStart());
		setAnimation(camin);
		boundAnim=null;
	}
	
	private void performBoundOverlayChangeAnim(float pl,float pt,float pr,float pb,final BoundOverlay next){
		BaseBoundOverlay tmp=new BaseBoundOverlay();
		tmp.setLeft(pl);
		tmp.setTop(pt);
		tmp.setRight(pr);
		tmp.setBottom(pb);
		boundOverlay=tmp;
		
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<BoundOverlay>(tmp,"left")
															 .transform(pl,0,Easing.None)
															 .transform(next.getLeft(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		builder.together(new FloatQueryAnimation<BoundOverlay>(tmp,"top")
						 .transform(pt,0,Easing.None)
						 .transform(next.getTop(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		builder.together(new FloatQueryAnimation<BoundOverlay>(tmp,"right")
						 .transform(pr,0,Easing.None)
						 .transform(next.getRight(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));													 
		builder.together(new FloatQueryAnimation<BoundOverlay>(tmp,"bottom")
						 .transform(pb,0,Easing.None)
						 .transform(next.getBottom(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		anim.start();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					boundOverlay=next;
				}
			});
		boundAnim=anim;
	}

	public void setBoundOverlay(BoundOverlay boundOverlay){
		if(boundOverlay==null)boundOverlay=initialBound;
		performBoundOverlayChangeAnim(getLeft(),getTop(),getRight(),getBottom(),boundOverlay);
		invalidateDraw();
	}

	public BoundOverlay getBoundOverlay(){
		return boundOverlay;
	}

	@Override
	public float getLeft(){
		// TODO: Implement this method
		if(boundOverlay!=null){
			return boundOverlay.getLeft()+getOffsetX();
		}
		return super.getLeft();
	}

	@Override
	public float getTop(){
		// TODO: Implement this method
		if(boundOverlay!=null){
			return boundOverlay.getTop()+getOffsetY();
		}
		return super.getTop();
	}

	@Override
	public float getRight(){
		// TODO: Implement this method
		if(boundOverlay!=null){
			return boundOverlay.getRight()+getOffsetX();
		}
		return super.getRight();
	}

	@Override
	public float getBottom(){
		// TODO: Implement this method
		if(boundOverlay!=null){
			return boundOverlay.getBottom()+getOffsetY();
		}
		return super.getBottom();
	}
	
	public void setInner(float w){
		centerRing.setRadius(w);
		pinkCover.setRadius(w);
		ring.setInnerRadius(w);
		shadowInner.setRadius(w);
		shadowInner.setInnerRadius(w-ViewConfiguration.dp(5));
		
		/*
		float p=w/(getWidth()/2*0.9f);
		for(PartRing r:rings){
			r.sprite.setScale(p);
		}
		*/
	}
	
	public void setRadius(float r){
		ring.setRadius(r);
		shadow.setInnerRadius(r-2);
	}
	
	public void startOpeningAnimation(final OnFinishListener l){
		setClickable(false);
		performingAnim=true;
		ring.resetRadius();
		pinkCover.resetRadius();
		centerRing.resetRadius();
		shadow.resetRadius();
		shadowInner.resetRadius();
		
		final float radius=getWidth()/2;
		
		ring.setPosition(getWidth()/2,getHeight()/2);
		ring.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
		pinkCover.setPosition(getWidth()/2,getHeight()/2);
		pinkCover.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
		centerRing.setPosition(getWidth()/2,getHeight()/2);
		centerRing.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
		
		float shadowWidthPx=ViewConfiguration.dp(maxShadowWidth);
		shadow.setPosition(getWidth()/2,getHeight()/2);
		shadow.setArea(RectF.ltrb(-getWidth()/2-shadowWidthPx,-getHeight()/2-shadowWidthPx,getWidth()/2+shadowWidthPx,getHeight()/2+shadowWidthPx));
		
		shadowInner.setPosition(getWidth()/2,getHeight()/2);
		shadowInner.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
		
		pinkCover.setAlpha(1);
		
		FloatQueryAnimation anim=new FloatQueryAnimation<JumpingCircle>(this,"radius");
		anim.transform(0,0,Easing.None);
		anim.transform(radius,500,Easing.OutQuad);
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(anim);
		{
			FloatQueryAnimation anim2=new FloatQueryAnimation<JumpingCircle>(JumpingCircle.this,"inner");
			anim2.transform(0,200,Easing.None);
			anim2.transform(radius*0.9f,300,Easing.OutQuad);
			builder.together(anim2,0);
		}
		/*
		builder.together(new FloatQueryAnimation<JumpingCircle>(this,new FloatInvokeSetter<JumpingCircle>(){
								 @Override
								 public void invoke(JumpingCircle target,float v){
									 // TODO: Implement this method
									 for(PartRing r:rings){
										 r.sprite.setAlpha(v);
									 }
								 }
							 })
							 .transform(0,0,Easing.None)
							 .transform(1,500,Easing.None)
							 );*/
		
		builder.together(new FloatQueryAnimation<JumpingCircle>(this,new FloatInvokeSetter<JumpingCircle>(){
								 @Override
								 public void invoke(JumpingCircle target,float v){
									 // TODO: Implement this method
									 for(PartRing r:rings){
										 r.sprite.setScale(v);
									 }
								 }
							 })
						 .transform(0,0,Easing.None)
						 .transform(1,500,Easing.OutQuad)
						 );

		ComplexAnimation camin=builder.build();
		camin.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					performingAnim=false;
					setClickable(true);
					if(l!=null)l.onFinish();
					
				}
			});
		camin.start();
		setAnimation(camin);
		
		
		ComplexAnimationBuilder builder2=ComplexAnimationBuilder.start(
			new FloatQueryAnimation<EdContainer>(getContext().getViewRoot().getRootContainer(),"alpha")
			.transform(0,0,Easing.None)
			.transform(1,300,Easing.InQuad));
		getContext().getViewRoot().getRootContainer().setAnimation(builder2.buildAndStart());
		RenderStatePopupView r=new RenderStatePopupView(getContext());
		r.show();
		RenderStatePopupView.setInstance(r);
	}

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		
		BaseScene s=LabGame.get().getScenes().getCurrentScene();
		if(s!=null){
			switch(s.getSceneName()){
				case ScenesName.SongSelect:
					
					return;
				case ScenesName.Edit:
					
					return;
			}
		}
		
		if(LabGame.get().getSceneSelectButtonBar().isHidden()){
			LabGame.get().getSceneSelectButtonBar().show();
		}else{
			LabGame.get().getSceneSelectButtonBar().update();
			switch(LabGame.get().getSceneSelectButtonBar().getCurrentGroupName()){
				case SceneSelectButtonBar.GROUP_MAIN:
					LabGame.get().getSceneSelectButtonBar().swapGroup(SceneSelectButtonBar.GROUP_PLAY);
					break;
				case SceneSelectButtonBar.GROUP_PLAY:
					LabGame.get().getSceneSelectButtonBar().getSoloClicker().onClick(null);
					break;
			}
			
		}
		
	}

	double glowDuration=800;
	public static double shadowClock;
	public static float glowProgress;
	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		
		if(!performingAnim){
			final float radius=getWidth()/2;

			ring.setPosition(getWidth()/2,getHeight()/2);
			ring.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
			pinkCover.setPosition(getWidth()/2,getHeight()/2);
			pinkCover.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
			centerRing.setPosition(getWidth()/2,getHeight()/2);
			centerRing.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));

			float shadowWidthPx=ViewConfiguration.dp(maxShadowWidth);
			shadow.setPosition(getWidth()/2,getHeight()/2);
			shadow.setArea(RectF.ltrb(-getWidth()/2-shadowWidthPx,-getHeight()/2-shadowWidthPx,getWidth()/2+shadowWidthPx,getHeight()/2+shadowWidthPx));

			shadowInner.setPosition(getWidth()/2,getHeight()/2);
			shadowInner.setArea(RectF.ltrb(-getWidth()/2,-getHeight()/2,getWidth()/2,getHeight()/2));
			final float w=radius*0.9f;
			centerRing.setRadius(w);
			pinkCover.setRadius(w);
			ring.setInnerRadius(w);
			shadowInner.setRadius(w);
			shadowInner.setInnerRadius(w-ViewConfiguration.dp(5));
			ring.setRadius(radius);
			shadow.setInnerRadius(radius-2);
		}
		
		RectF rt=RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight());
		double deltaTime=getContext().getFrameDeltaTime();
		
		for(PartRing r:rings){
			r.setArea(rt);
			r.updateTime(deltaTime);
		}
		
		shadowClock+=getContext().getFrameDeltaTime();
		shadowClock%=glowDuration*2;
		glowProgress=(float)Math.sqrt(Math.abs(shadowClock/glowDuration-1));
		float p=glowProgress;
		shadowWidth=p*maxShadowWidth;
		shadow.setRadius(shadow.getInnerRadius()+ViewConfiguration.dp(shadowWidth));
		shadow.draw(canvas);
		pinkCover.draw(canvas);
		
		for(PartRing r:rings){
			r.sprite.draw(canvas);
		}
		
		shadowInner.draw(canvas);
		ring.draw(canvas);
	}

	@Override
	public boolean inViewBound(float x,float y){
		// TODO: Implement this method
		return Vec2.length(x-(getLeft()+getRight())/2,y-(getTop()+getBottom())/2)<Math.min(getWidth(),getHeight())/2;
	}
	
	
	public class PartRing{
		
		public CircleSprite sprite;
		
		public float angle;
		
		public float innerRadius;
		
		public float radius;
		
		public float rotateSpeed;
		
		public float duration;
		
		public double time;
		
		public float iniAng;
		
		public float rate;
		
		public PartRing(float ang,float speed,float inner,float radius,float initialAng,Color4 color){
			this.innerRadius=inner;
			this.radius=radius;
			this.angle=ang;
			this.rotateSpeed=speed;
			this.rate=(rotateSpeed>0)?1:-1;
			this.duration=FMath.Pi2/Math.abs(rotateSpeed);
			this.iniAng=initialAng;
			sprite=new CircleSprite(getContext());
			sprite.setAccentColor(color);
		}
		
		/**
		 *一种支持0～120º的区域设置方法
		 */
		public void setArea(RectF r){
			float rd=r.getWidth()/2;
			sprite.setPosition(r.getLeft()+r.getWidth()/2,r.getTop()+r.getHeight()/2);
			Vec2 v1=new Vec2(rd/FMath.SIN60,0);
			Vec2 v2=v1.copy().rotate(angle);
			Vec2 v3=v1.copy().add(v2);
			sprite.setArea(new Quad(v2,v3,new Vec2(0,0),v1));
			
			sprite.setInnerRadius(rd*innerRadius);
			sprite.setRadius(rd*radius);
		}
		
		
		
		public void updateTime(double deltaTime){
			time=(deltaTime+time);
			sprite.setRotation(rate*(float)time/duration*FMath.Pi2+iniAng);
		}
	}
}
