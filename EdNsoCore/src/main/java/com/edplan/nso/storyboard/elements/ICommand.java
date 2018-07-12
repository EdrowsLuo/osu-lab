package com.edplan.nso.storyboard.elements;
import com.edplan.framework.ui.animation.Easing;

public interface ICommand extends Comparable<ICommand>
{
	public Easing getEasing();
	public void setEasing(Easing easing);
	
	public double getStartTime();
	public void setStartTime(double startTime);
	
	public double getEndTime();
	public void setEndTime(double endTime);
	
	public double getDuration();
}
