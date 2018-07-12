package com.edplan.osulab.ui.popup;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.osulab.ui.pieces.LabButton;
import com.edplan.osulab.ui.pieces.TextButton;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class PopupButtonGroup extends PopupView
{
	
	private boolean hideOnOutsidePress=true;
	
	private LinearLayout buttons;
	
	public PopupButtonGroup(MContext c){
		super(c);
		setOutsideTouchable(true);
		setClickable(true);
		setBackground(Color4.rgba(0,0,0,0.7f));
		{
			RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
			p.width=Param.MODE_MATCH_PARENT;
			p.height=Param.makeUpDP(LabButton.DEFAULT_HEIGHT+10);
			p.gravity=Gravity.Center;
			setLayoutParam(p);
		}
		{
			buttons=new LinearLayout(c);
			buttons.setOrientation(Orientation.DIRECTION_L2R);
			buttons.setChildoffset(ViewConfiguration.dp(5));
			RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
			p.width=Param.MODE_MATCH_PARENT;
			p.height=Param.MODE_MATCH_PARENT;
			//p.marginTop=ViewConfiguration.dp(10);
			//p.marginBottom=ViewConfiguration.dp(10);
			p.gravity=Gravity.Center;
			addView(buttons,p);
		}
	}
	
	public void addButton(String text,OnClickListener l){
		TextButton button=new TextButton(getContext());
		button.setText(text);
		button.setOnClickListener(l);
		MarginLayoutParam p=new MarginLayoutParam();
		p.width=Param.MODE_WRAP_CONTENT;
		p.height=Param.makeUpDP(LabButton.DEFAULT_HEIGHT);
		buttons.addView(button,p);
	}

	public void setHideOnOutsidePress(boolean hideOnOutsidePress){
		this.hideOnOutsidePress=hideOnOutsidePress;
	}

	public boolean isHideOnOutsidePress(){
		return hideOnOutsidePress;
	}

	@Override
	public boolean onOutsideTouch(EdMotionEvent e){
		// TODO: Implement this method
		if(hideOnOutsidePress){
			hide();
		}
		return true;
	}
	
	@Override
	protected void onHide(){
		// TODO: Implement this method
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		b.together(new FloatQueryAnimation<EdView>(this,"alpha")
				   .transform(getAlpha(),0,Easing.None)
				   .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=b.buildAndStart();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					PopupButtonGroup.super.onHide();
				}
			});
		setAnimation(anim);
	}

	@Override
	protected void onShow(){
		// TODO: Implement this method
		super.onShow();
		setAlpha(0);
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		b.together(new FloatQueryAnimation<EdView>(this,"alpha")
				   .transform(getAlpha(),0,Easing.None)
				   .transform(1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}
}
