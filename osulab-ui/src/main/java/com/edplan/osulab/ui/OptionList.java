package com.edplan.osulab.ui;
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
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.ScrollContainer;
import com.edplan.framework.ui.widget.TestButton;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.osulab.ui.pieces.LabCheckBox;
import com.edplan.osulab.ui.popup.RenderStatePopupView;
import com.edplan.osulab.ui.pieces.LabTextCheckBox;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.config.ConfigList;
import com.edplan.osulab.ui.config.DebugConfig;
import java.util.Iterator;
import com.edplan.framework.config.property.ConfigProperty;
import com.edplan.framework.config.ConfigEntry;
import com.edplan.framework.config.property.ConfigBoolean;

public class OptionList extends ScrollContainer implements Hideable
{
	private ColorRectSprite shadowSprite;
	
	public OptionList(MContext c){
		super(c);
		setOutsideTouchable(true);
		setChildoffset(ViewConfiguration.dp(1));
		setBackground(Color4.rgba(0,0,0,0.5f));
		setOrientation(Orientation.DIRECTION_T2B);
		setPaddingLeft(ViewConfiguration.dp(15));
		setPaddingRight(ViewConfiguration.dp(10));
		setGravity(Gravity.TopLeft);
		
		shadowSprite=new ColorRectSprite(c);
		shadowSprite.setColor(Color4.rgba(0,0,0,0.6f),
							  Color4.rgba(0,0,0,0f),
							  Color4.rgba(0,0,0,0.6f),
							  Color4.rgba(0,0,0,0f));
		
		loadConfig(DebugConfig.get());
		
		
	}
	
	public void loadConfig(ConfigList list){
		final MContext c=getContext();
		{
			TextView b=new TextView(c);
			b.setFont(BMFont.Exo_20_Semi_Bold);
			b.setGravity(Gravity.CenterLeft);
			b.setTextSize(ViewConfiguration.dp(30));
			b.setText(String.format("[%s]",list.getListName()));
			MarginLayoutParam param=new MarginLayoutParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.MODE_WRAP_CONTENT;
			addView(b,param);
		}
		
		Iterator<ConfigEntry> configs=list.iterateProperty();
		while(configs.hasNext()){
			final ConfigEntry e=configs.next();
			switch(e.getType()){
				case ConfigBoolean.TYPE:
					buildBoolean(e);
					break;
			}
		}
		
	}
	
	protected void buildBoolean(final ConfigEntry e){
		final MContext c=getContext();
		{
			LabTextCheckBox b=new LabTextCheckBox(c);
			b.setText(e.getName());
			b.setChecked(e.asBoolean().get());
			MarginLayoutParam param=new MarginLayoutParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.MODE_WRAP_CONTENT;
			param.marginTop=ViewConfiguration.dp(5);
			b.setOnCheckListener(new OnCheckListener(){
					@Override
					public void onCheck(boolean c,EdView view){
						// TODO: Implement this method
						e.asBoolean().set(c);
					}
				});
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
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<OptionList>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<OptionList>(this,"offsetX")
						 .transform(getOffsetX(),0,Easing.None)
						 .transform(-getWidth(),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=builder.build();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					setVisiblility(VISIBILITY_GONE);
					BackQuery.get().unregist(OptionList.this);
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
		if(!isHidden())return;
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<OptionList>(this,"alpha")
																	  .transform(getAlpha(),0,Easing.None)
																	  .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		builder.together(new FloatQueryAnimation<OptionList>(this,"offsetX")
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
		shadowSprite.setArea(RectF.xywh(canvas.getWidth(),0,ViewConfiguration.dp(9),canvas.getHeight()));
		shadowSprite.draw(canvas);
	}
}
