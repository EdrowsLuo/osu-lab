package com.edplan.osulab.ui.scenes;
import com.edplan.framework.Framework;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.EdView;
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
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.RelativeLayout.RelativeParam;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.ui.text.font.FontAwesome;
import java.util.HashMap;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.osulab.ui.pieces.JumpingCircle;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.popup.PopupToast;

public class SceneSelectButtonBar extends RelativeContainer implements Hideable,BackQuery.BackHandler
{
	public static final String GROUP_MAIN="main";
	
	public static final String GROUP_PLAY="play";
	
	private HashMap<String,ButtonGroup> groups=new HashMap<String,ButtonGroup>();
	
	private float buttonSize=100;
	
	private float dividePoint=0.3f;
	
	private float circleSize=0.25f;
	
	private float shadowDp=9;

	private LinearLayout leftLayout;
	
	private LinearLayout rightLayout;
	
	private ButtonGroup currentGroup;
	
	private ColorRectSprite shadowSpriteTop,shadowSpriteBottom;
	
	private String currentGroupName=GROUP_MAIN;
	
	private OnClickListener soloClicker;
	
	public SceneSelectButtonBar(MContext c){
		super(c);
		currentGroup=new ButtonGroup();
		shadowSpriteTop=new ColorRectSprite(c);
		shadowSpriteTop.setColor(Color4.rgba(0,0,0,0),
							  Color4.rgba(0,0,0,0),
							  Color4.rgba(0,0,0,0.2f),
							  Color4.rgba(0,0,0,0.2f));
		shadowSpriteBottom=new ColorRectSprite(c);
		shadowSpriteBottom.setColor(Color4.rgba(0,0,0,0.5f),
								 Color4.rgba(0,0,0,0.5f),
								 Color4.rgba(0,0,0,0),
								 Color4.rgba(0,0,0,0));
		
		setBackground(UiConfig.Color.BLUE_DEEP_DARK.copyNew().setAlpha(0.9f));
		{
			leftLayout=new LinearLayout(c);
			leftLayout.setBackwardsDraw(true);
			leftLayout.setOrientation(Orientation.DIRECTION_R2L);
			RelativeParam param=new RelativeParam();
			param.width=Param.makeupScaleOfParentParam(dividePoint);
			param.height=Param.MODE_MATCH_PARENT;
			param.gravity=Gravity.CenterLeft;
			addView(leftLayout,param);
		}
		
		{
			rightLayout=new LinearLayout(c);
			rightLayout.setBackwardsDraw(true);
			rightLayout.setOrientation(Orientation.DIRECTION_R2L);
			rightLayout.setGravity(Gravity.CenterLeft);
			RelativeParam param=new RelativeParam();
			param.width=Param.makeupScaleOfParentParam(1-dividePoint-0.1f);
			param.height=Param.MODE_MATCH_PARENT;
			param.gravity=Gravity.CenterRight;
			addView(rightLayout,param);
		}
		
		{
			//main
			{
				currentGroup.leftWrapper=leftLayout.getChildrenWrapper();
				{
					SceneSelectButton button=new SceneSelectButton(c);
					button.setBackgroundColor(Color4.gray(0.2f));
					button.setLeftShadow();
					button.setText("Setting");
					button.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(EdView view){
								// TODO: Implement this method
								LabGame.get().getOptionList().show();
							}
						});
					button.setTexture(FontAwesome.fa_cogs.getTexture());
					MarginLayoutParam p=new MarginLayoutParam();
					p.height=Param.MODE_MATCH_PARENT;
					p.width=Param.makeUpDP(buttonSize);
					leftLayout.addView(button,p);
				}
			}
			{
				currentGroup.rightWrapper=rightLayout.getChildrenWrapper();
				{
					{
						SceneSelectButton button=new SceneSelectButton(c);
						button.setBackgroundColor(UiConfig.Color.BLUE);
						button.setTexture(FontAwesome.fa_osu_logo.getTexture());
						button.setText("Play");
						button.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(EdView view){
									// TODO: Implement this method
									swapGroup(GROUP_PLAY);
								}
							});
						MarginLayoutParam p=new MarginLayoutParam();
						p.height=Param.MODE_MATCH_PARENT;
						p.width=Param.makeUpDP(buttonSize);
						rightLayout.addView(button,p);
					}
					{
						SceneSelectButton button=new SceneSelectButton(c);
						button.setBackgroundColor(UiConfig.Color.YELLOW);
						button.setTexture(FontAwesome.fa_osu_chevron_down_o.getTexture());
						button.setText("Download");
						button.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(EdView view){
									// TODO: Implement this method
									PopupToast.toast(getContext(),"working").show();
								}
							});
						MarginLayoutParam p=new MarginLayoutParam();
						p.height=Param.MODE_MATCH_PARENT;
						p.width=Param.makeUpDP(buttonSize);
						rightLayout.addView(button,p);
					}
					{
						SceneSelectButton button=new SceneSelectButton(c);
						button.setBackgroundColor(UiConfig.Color.PINK);
						button.setTexture(FontAwesome.fa_osu_cross_o.getTexture());
						button.setText("Exit");
						button.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(EdView view){
									// TODO: Implement this method
									hide();
									LabGame.get().exit();
								}
							});
						MarginLayoutParam p=new MarginLayoutParam();
						p.height=Param.MODE_MATCH_PARENT;
						p.width=Param.makeUpDP(buttonSize);
						rightLayout.addView(button,p);
					}
				}
			}
			groups.put(GROUP_MAIN,currentGroup);
		}
		{
			currentGroup=new ButtonGroup();
			leftLayout.setChildrenWrapper(new ChildrenWrapper());
			rightLayout.setChildrenWrapper(new ChildrenWrapper());
			//play
			{
				currentGroup.leftWrapper=leftLayout.getChildrenWrapper();
				{
					SceneSelectButton button=new SceneSelectButton(c);
					button.setBackgroundColor(Color4.gray(0.3f));
					button.setLeftShadow();
					button.setText("Back");
					button.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(EdView view){
								// TODO: Implement this method
								swapGroup(GROUP_MAIN);
							}
						});
					button.setTexture(FontAwesome.fa_backward.getTexture());
					MarginLayoutParam p=new MarginLayoutParam();
					p.height=Param.MODE_MATCH_PARENT;
					p.width=Param.makeUpDP(buttonSize);
					leftLayout.addView(button,p);
				}
			}
			{
				currentGroup.rightWrapper=rightLayout.getChildrenWrapper();
				{
					{
						SceneSelectButton button=new SceneSelectButton(c);
						button.setBackgroundColor(UiConfig.Color.BLUE_DEEP_DARK);
						button.setTexture(FontAwesome.fa_osu_logo.getTexture());
						button.setText("Solo");
						button.setOnClickListener(soloClicker=new OnClickListener(){
								@Override
								public void onClick(EdView view){
									// TODO: Implement this method
									hide();
									LabGame.get().getScenes().swapScene(ScenesName.SongSelect);
								}
							});
						MarginLayoutParam p=new MarginLayoutParam();
						p.height=Param.MODE_MATCH_PARENT;
						p.width=Param.makeUpDP(buttonSize);
						rightLayout.addView(button,p);
					}
					{
						SceneSelectButton button=new SceneSelectButton(c);
						button.setBackgroundColor(UiConfig.Color.BLUE_DARK);
						button.setTexture(FontAwesome.fa_osu_multi.getTexture());
						button.setText("Multiple");
						button.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(EdView view){
									// TODO: Implement this method
									PopupToast.toast(getContext(),"working").show();
								}
							});
						MarginLayoutParam p=new MarginLayoutParam();
						p.height=Param.MODE_MATCH_PARENT;
						p.width=Param.makeUpDP(buttonSize);
						rightLayout.addView(button,p);
					}
					{
						SceneSelectButton button=new SceneSelectButton(c);
						button.setBackgroundColor(UiConfig.Color.BLUE_LIGHT);
						button.setTexture(FontAwesome.fa_osu_edit_o.getTexture());
						button.setText("Edit");
						button.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(EdView view){
									// TODO: Implement this method
									PopupToast.toast(getContext(),"working").show();
									//hide();
									//LabGame.get().getScenes().swapScene(ScenesName.Edit);
								}
							});
						MarginLayoutParam p=new MarginLayoutParam();
						p.height=Param.MODE_MATCH_PARENT;
						p.width=Param.makeUpDP(buttonSize);
						rightLayout.addView(button,p);
					}
				}
			}
			groups.put(GROUP_PLAY,currentGroup);
		}
		applyGroup(getGroup(GROUP_MAIN));
	}

	public OnClickListener getSoloClicker(){
		return soloClicker;
	}

	public String getCurrentGroupName(){
		return currentGroupName;
	}
	
	private ButtonGroup getGroup(String name){
		return groups.get(name);
	}
	
	protected void applyGroup(ButtonGroup g){
		leftLayout.setChildrenWrapper(g.leftWrapper);
		rightLayout.setChildrenWrapper(g.rightWrapper);
		currentGroup=g;
		invalidateDraw();
	}
	
	protected void directSwap(String name){
		currentGroupName=name;
		applyGroup(getGroup(name));
	}
	
	public void swapGroup(final String name){
		final ButtonGroup g=getGroup(name);
		if(g==null||currentGroup==g)return;
		for(EdView v:currentGroup.leftWrapper){
			((SceneSelectButton)v).hide();
		}

		for(EdView v:currentGroup.rightWrapper){
			((SceneSelectButton)v).hide();
		}
		
		post(new Runnable(){
				@Override
				public void run(){
					// TODO: Implement this method
					directSwap(name);
					currentGroup.show();
					//PopupToast.toast(getContext(),"swap to "+name).show();
				}
			},SceneSelectButton.ANIM_DURATION);
	}

	@Override
	public boolean onBack(){
		// TODO: Implement this method
		switch(currentGroupName){
			case GROUP_MAIN:
				return false;
			case GROUP_PLAY:
				swapGroup(GROUP_MAIN);
				return true;
		}
		return false;
	}
	

	@Override
	public void onInitialLayouted(){
		// TODO: Implement this method
		super.onInitialLayouted();
		directHide();
	}
	
	protected void directHide(){
		setVisiblility(VISIBILITY_GONE);
		setAlpha(0);
		invalidateDraw();
	}
	
	
	@Override
	public void hide(){
		// TODO: Implement this method
		BackQuery.get().unregist(this);
		LabGame.get().getJumpingCircle().setBoundOverlay(null);
		
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButtonBar>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
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
		
		currentGroup.hide();
		
	}

	@Override
	public void show(){
		// TODO: Implement this method
		BackQuery.get().registNoBackButton(this);
		directSwap(GROUP_MAIN);
		BaseBoundOverlay bound=new BaseBoundOverlay();
		RectF area=RectF.anchorOWH(Anchor.Center,
								   getLeft()+getWidth()*dividePoint,
								   (getTop()+getBottom())/2,
								   getWidth()*circleSize,
								   getWidth()*circleSize);
		bound.setLeft(area.getLeft());
		bound.setTop(area.getTop());
		bound.setRight(area.getRight());
		bound.setBottom(area.getBottom());
		LabGame.get().getJumpingCircle().setBoundOverlay(bound);
		
		
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<SceneSelectButtonBar>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					update();
					delayHide(5000);
				}
			});
		anim.start();
		setAnimation(anim);
		
		currentGroup.show();
		
		setVisiblility(VISIBILITY_SHOW);
	}

	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}
	
	private void delayHide(double delay){
		post(new Runnable(){
				@Override
				public void run(){
					// TODO: Implement this method
					if(Framework.relativePreciseTimeMillion()-updateClock>10000){
						if(!isHidden())hide();
					}else{
						delayHide(2000);
					}
				}
			},delay);
	}
	
	public void update(){
		updateClock=Framework.relativePreciseTimeMillion();
	}

	double updateClock;
	@Override
	public boolean onMotionEvent(EdMotionEvent e){
		// TODO: Implement this method
		update();
		return super.onMotionEvent(e);
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		
		float shadowScale=0.5f+JumpingCircle.glowProgress/2;
		
		shadowSpriteTop.setArea(RectF.anchorOWH(Anchor.BottomLeft,0,0,canvas.getWidth(),ViewConfiguration.dp(shadowDp*shadowScale)*0.5f));
		shadowSpriteTop.draw(canvas);
		
		shadowSpriteBottom.setArea(RectF.anchorOWH(Anchor.TopLeft,0,canvas.getHeight(),canvas.getWidth(),ViewConfiguration.dp(shadowDp*shadowScale)));
		shadowSpriteBottom.draw(canvas);
		
		
		super.onDraw(canvas);
	}
	
	private class ButtonGroup implements Hideable
	{
		public ChildrenWrapper leftWrapper,rightWrapper;
		public Runnable onHide;
		public Runnable onShow;
		
		@Override
		public void hide(){
			// TODO: Implement this method
			if(onHide!=null)onHide.run();
			for(EdView v:leftWrapper){
				((SceneSelectButton)v).hide();
			}

			for(EdView v:rightWrapper){
				((SceneSelectButton)v).hide();
			}
		}

		@Override
		public void show(){
			// TODO: Implement this method
			if(onShow!=null)onShow.run();
			for(EdView v:leftWrapper){
				((SceneSelectButton)v).show();
			}

			for(EdView v:rightWrapper){
				((SceneSelectButton)v).show();
			}
		}

		@Override
		public boolean isHidden(){
			// TODO: Implement this method
			return currentGroup!=this;
		}
	}
}
