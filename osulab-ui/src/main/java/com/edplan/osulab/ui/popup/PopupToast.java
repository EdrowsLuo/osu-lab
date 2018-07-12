package com.edplan.osulab.ui.popup;
import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;

public class PopupToast extends PopupView
{
	private float hideOffsetX=ViewConfiguration.dp(100);
	
	TextView text;
	
	public PopupToast(MContext c){
		super(c);
		setRounded(ViewConfiguration.dp(5));
		//.setShadow(ViewConfiguration.dp(3),Color4.rgba(0.5f,0.5f,0.5f,0.3f),Color4.Alpha);
		LinearLayout l=new LinearLayout(c);
		l.setGravity(Gravity.Center);
		l.setBackground(Color4.rgba(0,0,0,0.4f));
		text=new TextView(c);
		text.setTextSize(ViewConfiguration.dp(17));
		RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
		p.width=Param.MODE_WRAP_CONTENT;
		p.height=Param.MODE_WRAP_CONTENT;
		p.gravity=Gravity.BottomCenter;
		p.marginBottom=ViewConfiguration.dp(60);
		setLayoutParam(p);
		{
			RelativeLayout.RelativeParam pl=new RelativeLayout.RelativeParam();
			pl.width=Param.MODE_WRAP_CONTENT;
			pl.height=Param.MODE_WRAP_CONTENT;
			pl.gravity=Gravity.BottomCenter;
			addView(l,pl);
		}
		{
			MarginLayoutParam pl=new MarginLayoutParam();
			pl.width=Param.MODE_WRAP_CONTENT;
			pl.height=Param.MODE_WRAP_CONTENT;
			pl.postMargin(ViewConfiguration.dp(6));
			pl.marginLeft=ViewConfiguration.dp(10);
			pl.marginRight=ViewConfiguration.dp(10);
			l.addView(text,pl);
		}
	}

	@Override
	protected void onHide(){
		// TODO: Implement this method
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		b.together(new FloatQueryAnimation<EdView>(this,"offsetY")
				   .transform(getOffsetY(),0,Easing.None)
				   .transform(hideOffsetX,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=b.buildAndStart();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					PopupToast.super.onHide();
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
		b.together(new FloatQueryAnimation<EdView>(this,"offsetY")
				   .transform(hideOffsetX,0,Easing.None)
				   .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.OutQuad));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
		
		post(new Runnable(){
				@Override
				public void run(){
					// TODO: Implement this method
					hide();
				}
			},1500);
		
	}
	
	public void setText(String t){
		text.setText(t);
	}
	
	public static PopupToast toast(MContext c,String text){
		PopupToast t=new PopupToast(c);
		t.setText(text);
		return t;
	}
	
}
