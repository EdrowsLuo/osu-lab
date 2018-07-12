package com.edplan.nso;
import com.edplan.nso.storyboard.Storyboard;


public abstract class NsoBeatmap
{
	private Storyboard storyboard;

	public void setStoryboard(Storyboard storyboard) {
		this.storyboard=storyboard;
	}

	public Storyboard getStoryboard() {
		return storyboard;
	}
}
