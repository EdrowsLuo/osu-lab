package com.edplan.osulab.ui.toolbar;
import com.edplan.framework.Framework;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.drawable.ColorDrawable;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.ui.widget.FontAwesomeTextView;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.RelativeLayout.RelativeParam;
import com.edplan.framework.ui.widget.TestButton;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ui.OptionList;
import com.edplan.osulab.ui.pieces.TextButton;
import com.edplan.osulab.ui.pieces.SongPanel;
import com.edplan.osulab.ui.popup.PopupToast;

public class Toolbar extends RelativeContainer implements Hideable
{
	private float normalBaseAlpha=0.7f;
	
	private float highlightBaseAlpha=1;
	
	private float baseAlpha=normalBaseAlpha;
	
	private float settedAlpha=1;
	
	private float normalShadowHeight=7;
	
	private float highlightShadowHeight=40;
	
	private float shadowHeight=7;
	
	private boolean highlight=false;
	
	private LinearLayout leftLayout;
	
	private LinearLayout rightLayout;
	
	private ColorRectSprite shadowSprite;
	
	private double preTouchTime;
	
	public final ToolbarShadow shadow;
	
	public Toolbar(MContext c){
		super(c);
		shadow=new ToolbarShadow(c);
		setClickable(true);
		setAccentColor(Color4.rgba(1,1,1,1f));
		ColorDrawable cd=new ColorDrawable(c);
		cd.setColor(Color4.rgba(0,0,0,0.5f),
					Color4.rgba(0,0,0,0.5f),
					Color4.rgba(0,0,0,0.5f),
					Color4.rgba(0,0,0,0.5f));
		setBackground(cd);
		shadowSprite=new ColorRectSprite(c);
		float gr=0f;
		Color4 dividerColor=Color4.rgba(1,1,1,0.4f);
		shadowSprite.setColor(Color4.rgba(gr,gr,gr,0.6f),
							  Color4.rgba(gr,gr,gr,0.6f),
							  Color4.rgba(0,0,0,0f),
							  Color4.rgba(0,0,0,0f));
		{
			leftLayout=new LinearLayout(c);
			leftLayout.setGravity(Gravity.CenterLeft);
			leftLayout.setOrientation(Orientation.DIRECTION_L2R);
			RelativeParam param=new RelativeParam();
			param.gravity=Gravity.CenterLeft;
			param.width=Param.MODE_WRAP_CONTENT;
			param.height=Param.MODE_MATCH_PARENT;
			addView(leftLayout,param);
			
			{
				ToolBarButton msgShowButton=new ToolBarButton(c);
				msgShowButton.setIcon(FontAwesome.fa_navicon.getTexture());
				msgShowButton.setGravity(Gravity.Center);
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeupScaleOfParentOtherParam(1.6f);
				lparam.height=Param.MODE_MATCH_PARENT;
				msgShowButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(EdView view){
							// TODO: Implement this method
							OptionList list=LabGame.get().getOptionList();
							if(list.getVisiblility()==VISIBILITY_GONE){
								list.show();
							}else{
								list.hide();
							}
						}
					});
				leftLayout.addView(msgShowButton,lparam);
			}
			{
				EdView divider=new EdView(c);
				divider.setBackground(dividerColor);
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeUpDP(2f);
				lparam.height=Param.MODE_MATCH_PARENT;
				lparam.marginBottom=ViewConfiguration.dp(4);
				lparam.marginTop=ViewConfiguration.dp(4);
				leftLayout.addView(divider,lparam);
			}
		}
		{
			rightLayout=new LinearLayout(c);
			rightLayout.setOrientation(Orientation.DIRECTION_L2R);
			RelativeParam param=new RelativeParam();
			param.gravity=Gravity.CenterRight;
			param.width=Param.MODE_WRAP_CONTENT;
			param.height=Param.MODE_MATCH_PARENT;
			addView(rightLayout,param);
			
			{
				ToolBarButton msgShowButton=new ToolBarButton(c);
				msgShowButton.setGravity(Gravity.Center);
				msgShowButton.setIcon(FontAwesome.fa_music.getTexture());
				msgShowButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(EdView view){
							// TODO: Implement this method
							SongPanel panel=SongPanel.getInstance();
							if(panel==null){
								panel=new SongPanel(getContext());
								SongPanel.setInstance(panel);
								panel.show();
								//PopupToast.toast(getContext(),"create show").show();
							}else{
								//PopupToast.toast(getContext(),""+panel.isHidden()).show();
								if(panel.isHidden()){
									panel.show();
								}else{
									panel.hide();
									//SongPanel.setInstance(null);
								}
							}
						}
					});
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeUpDP(50);
				lparam.height=Param.MODE_MATCH_PARENT;
				rightLayout.addView(msgShowButton,lparam);
			}
			{
				EdView divider=new EdView(c);
				divider.setBackground(dividerColor);
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeUpDP(2f);
				lparam.height=Param.MODE_MATCH_PARENT;
				lparam.marginBottom=ViewConfiguration.dp(4);
				lparam.marginTop=ViewConfiguration.dp(4);
				rightLayout.addView(divider,lparam);
			}
			{
				ToolBarButton msgShowButton=new ToolBarButton(c);
				msgShowButton.setGravity(Gravity.Center);
				msgShowButton.setIcon(FontAwesome.fa_angle_double_down.getTexture());
				msgShowButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(EdView view){
							// TODO: Implement this method
							if(LabGame.get().getSceneOverlay().getVisiblility()!=VISIBILITY_GONE){
								LabGame.get().getSceneOverlay().hide();
							}else{
								LabGame.get().getSceneOverlay().show();
							}
						}
					});
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeUpDP(50);
				lparam.height=Param.MODE_MATCH_PARENT;
				rightLayout.addView(msgShowButton,lparam);
			}
			{
				EdView divider=new EdView(c);
				divider.setBackground(dividerColor);
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeUpDP(2f);
				lparam.height=Param.MODE_MATCH_PARENT;
				lparam.marginBottom=ViewConfiguration.dp(4);
				lparam.marginTop=ViewConfiguration.dp(4);
				rightLayout.addView(divider,lparam);
			}
			{
				ToolBarButton msgShowButton=new ToolBarButton(c);
				msgShowButton.setGravity(Gravity.Center);
				msgShowButton.setIcon(FontAwesome.fa_genderless.getTexture());
				MarginLayoutParam lparam=new MarginLayoutParam();
				lparam.width=Param.makeUpDP(40);
				lparam.height=Param.MODE_MATCH_PARENT;
				rightLayout.addView(msgShowButton,lparam);
				msgShowButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(EdView view){
							// TODO: Implement this method
							if(!LabGame.get().getMessageList().isHidden()){
								LabGame.get().getMessageList().hide();
							}else{
								LabGame.get().getMessageList().show();
							}
						}
					});
			}
		}
		setAlpha(1);
	}

	@Override
	public void onInitialLayouted(){
		// TODO: Implement this method
		super.onInitialLayouted();
		directHide();
	}

	@Override
	public boolean onMotionEvent(EdMotionEvent e){
		// TODO: Implement this method
		preTouchTime=Framework.relativePreciseTimeMillion();
		if(!highlight){
			highlightOn();
		}
		return super.onMotionEvent(e);
	}

	public void setShadowHeight(float shadowHeight){
		this.shadowHeight=shadowHeight;
	}

	public float getShadowHeight(){
		return shadowHeight;
	}
	
	@Override
	public void hide(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<Toolbar>(this,"offsetY")
						 .transform(getOffsetY(),0,Easing.None)
						 .transform(-getHeight(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
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
		setOffsetY(-getHeight());
		setAlpha(0);
	}
	
	@Override
	public void show(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<Toolbar>(this,"offsetY")
						 .transform(getOffsetY(),0,Easing.None)
						 .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
		setVisiblility(VISIBILITY_SHOW);
	}
	
	private void postHighlightOff(double offset){
		post(new Runnable(){
				@Override
				public void run(){
					// TODO: Implement this method
					if(Framework.relativePreciseTimeMillion()-preTouchTime>700){
						highlightOff();
					}else{
						postHighlightOff(100);
					}
				}
			},offset);
	}
	
	public void highlightOn(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this,"baseAlpha")
																	  .transform(getBaseAlpha(),0,Easing.None)
																	  .transform(highlightBaseAlpha,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<Toolbar>(this,"shadowHeight")
						. transform(getShadowHeight(),0,Easing.None)
						. transform(highlightShadowHeight,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					postHighlightOff(1000);
				}
			});
		anim.start();
		setAnimation(anim);
		highlight=true;
	}
	
	public void highlightOff(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<Toolbar>(this,"baseAlpha")
																	  .transform(getBaseAlpha(),0,Easing.None)
																	  .transform(normalBaseAlpha,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<Toolbar>(this,"shadowHeight")
						 . transform(getShadowHeight(),0,Easing.None)
						 . transform(normalShadowHeight,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					highlight=false;
				}
			});
		anim.start();
		setAnimation(anim);
	}

	
	
	public void setBaseAlpha(float baseAlpha){
		this.baseAlpha=baseAlpha;
		updateAlpha();
	}

	public float getBaseAlpha(){
		return baseAlpha;
	}

	@Override
	public void setAlpha(float alpha){
		// TODO: Implement this method
		settedAlpha=alpha;
		shadowSprite.setAlpha(alpha);
		updateAlpha();
	}

	@Override
	public float getAlpha(){
		// TODO: Implement this method
		return settedAlpha;
	}
	
	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}
	
	protected void updateAlpha(){
		super.setAlpha(settedAlpha*baseAlpha);
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		
	}
	
	public class ToolbarShadow extends EdView{
		
		public ToolbarShadow(MContext c){
			super(c);
		}

		@Override
		public float getOffsetX(){
			// TODO: Implement this method
			return Toolbar.this.getOffsetX();
		}

		@Override
		public float getOffsetY(){
			// TODO: Implement this method
			return Toolbar.this.getOffsetY();
		}

		@Override
		protected void onDraw(BaseCanvas canvas){
			// TODO: Implement this method
			super.onDraw(canvas);
			shadowSprite.setArea(RectF.xywh(0,canvas.getHeight(),canvas.getWidth(),ViewConfiguration.dp(shadowHeight)));
			shadowSprite.draw(canvas);
		}
	}
}
