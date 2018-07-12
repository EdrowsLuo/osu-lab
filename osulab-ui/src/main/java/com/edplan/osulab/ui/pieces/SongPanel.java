package com.edplan.osulab.ui.pieces;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.TextureView;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.osulab.ui.popup.PopupToast;
import com.edplan.framework.ui.inputs.EdMotionEvent;

public class SongPanel extends PopupView
{
	private static SongPanel instance;
	
	private TextureView background;
	
	private float hideOffset=ViewConfiguration.dp(100);
	
	public SongPanel(MContext c){
		super(c);
		//setOutsideTouchable(false);
		{
			RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
			p.width=Param.makeUpDP(300);
			p.height=Param.makeUpDP(150);
			p.gravity=Gravity.TopRight;
			p.marginTop=ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP+10);
			p.marginRight=ViewConfiguration.dp(10);
			setLayoutParam(p);
		}
		setBackground(Color4.Black);
		setRounded(ViewConfiguration.dp(20));
		setClickable(true);
		setOutsideTouchable(true);
		/* .setShadow(
			ViewConfiguration.dp(5),
			Color4.rgba(1,1,1,0.3f),
			Color4.rgba(0,0,0,0));*/
		{
			background=new TextureView(c);
			RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
			p.width=Param.MODE_MATCH_PARENT;
			p.height=Param.MODE_MATCH_PARENT;
		}
	}

	@Override
	public boolean onOutsideTouch(EdMotionEvent e){
		// TODO: Implement this method
		hide();
		return true;
	}
	
	@Override
	protected void onHide(){
		// TODO: Implement this method
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		b.together(new FloatQueryAnimation<EdView>(this,"offsetX")
				   .transform(0,0,Easing.None)
				   .transform(hideOffset,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=b.buildAndStart();
		anim.setOnFinishListener(new OnFinishListener(){
				@Override
				public void onFinish(){
					// TODO: Implement this method
					SongPanel.super.onHide();
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
		b.together(new FloatQueryAnimation<EdView>(this,"offsetX")
				   .transform(hideOffset,0,Easing.None)
				   .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME*2,Easing.OutBounce));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}

	public static void setInstance(SongPanel instance){
		SongPanel.instance=instance;
	}

	public static SongPanel getInstance(){
		return instance;
	}
}
