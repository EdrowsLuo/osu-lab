package com.edplan.nso.storyboard.elements;
import com.edplan.nso.storyboard.elements.drawable.BaseDrawableSprite;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.nso.storyboard.elements.drawable.ADrawableStoryboardElement;

public class StoryboardSample implements IStoryboardElements
{
	private String path;
	
	private double time;
	private float volume;
	
	public StoryboardSample(String path,double time,float volume){
		this.path=path;
		this.time=time;
		this.volume=volume;
	}

	public void setTime(double time) {
		this.time=time;
	}

	public double getTime() {
		return time;
	}

	public void setVolume(float volume) {
		this.volume=volume;
	}

	public float getVolume() {
		return volume;
	}

	@Override
	public void onApply(ADrawableStoryboardElement sprite,PlayingStoryboard storyboard) {

	}

	@Override
	public boolean isDrawable() {

		return false;
	}
	
	public void setPath(String path){
		this.path=path;
	}

	@Override
	public String getPath() {

		return path;
	}

	@Override
	public String[] getTexturePaths() {

		return new String[0];
	}

	@Override
	public BaseDrawableSprite createDrawable(PlayingStoryboard storyboard) {

		return null;
	}
}
