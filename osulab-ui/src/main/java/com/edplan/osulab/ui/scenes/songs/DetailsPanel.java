package com.edplan.osulab.ui.scenes.songs;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.component.Hideable;

public class DetailsPanel extends RelativeContainer implements Hideable
{
	public static float WIDTH_DP=350;
	
	public DetailsPanel(MContext c){
		super(c);
		setDebug(true);
	}
	
	@Override
	public void hide(){
		// TODO: Implement this method
		ComplexAnimationBuilder b=ComplexAnimationBuilder.
			//start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
			start(new FloatQueryAnimation<EdView>(this,"offsetX")
				  .transform(0,0,Easing.None)
				  .transform(-ViewConfiguration.dp(WIDTH_DP),ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.InQuad));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}

	@Override
	public void show(){
		// TODO: Implement this method
		setAlpha(0);
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		b.together(new FloatQueryAnimation<EdView>(this,"offsetX")
				   .transform(-ViewConfiguration.dp(WIDTH_DP),0,Easing.None)
				   .transform(0,ViewConfiguration.DEFAULT_TRANSITION_TIME*2,Easing.OutQuad));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}


	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}
	
}
