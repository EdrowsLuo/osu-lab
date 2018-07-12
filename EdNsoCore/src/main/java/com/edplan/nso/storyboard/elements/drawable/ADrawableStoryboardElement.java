package com.edplan.nso.storyboard.elements.drawable;
import com.edplan.framework.fallback.GLES10Drawable;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.framework.graphics.opengl.fast.FastRenderer;
import com.edplan.nso.storyboard.renderer.OsbRenderer;

public abstract class ADrawableStoryboardElement extends EdDrawable implements GLES10Drawable
{
	private PlayingStoryboard storyboard;
	
	public ADrawableStoryboardElement(PlayingStoryboard storyboard){
		super(storyboard.getContext());
		this.storyboard=storyboard;
	}
	
	public PreciseTimeline getTimeline(){
		return storyboard.getTimeline();
	}

	public void setStoryboard(PlayingStoryboard storyboard) {
		this.storyboard=storyboard;
	}

	public PlayingStoryboard getStoryboard() {
		return storyboard;
	}
	
	public abstract void drawOsbRenderer(OsbRenderer renderer);
	
	public abstract void drawFastRenderer(FastRenderer renderer);
	
	public abstract void prepareForDraw();
	
	public abstract void onAdd();
	
	public abstract void onRemove();
	
	public abstract double getStartTime();
	
	public abstract double getEndTime();
	
	public abstract boolean hasAdd();
}
