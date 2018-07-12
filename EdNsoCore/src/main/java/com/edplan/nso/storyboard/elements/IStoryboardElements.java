package com.edplan.nso.storyboard.elements;
import com.edplan.nso.storyboard.elements.drawable.BaseDrawableSprite;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.nso.storyboard.elements.drawable.ADrawableStoryboardElement;

public interface IStoryboardElements
{
	public boolean isDrawable();
	
	public String getPath();
	
	public String[] getTexturePaths();
	
	public BaseDrawableSprite createDrawable(PlayingStoryboard storyboard);
	
	public void onApply(ADrawableStoryboardElement sprite,PlayingStoryboard storyboard);
}
