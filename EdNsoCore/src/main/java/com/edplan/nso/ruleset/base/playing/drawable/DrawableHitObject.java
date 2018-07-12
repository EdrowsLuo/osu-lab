package com.edplan.nso.ruleset.base.playing.drawable;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.nso.ruleset.base.playing.Judgment;
import com.edplan.framework.ui.drawable.interfaces.IFadeable;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.MContext;

public abstract class DrawableHitObject extends EdDrawable implements IFadeable
{
	public DrawableHitObject(MContext c){
		super(c);
	}
	
	/**
	 *这个物件出现的时间，此处的时间为getTimeLine()中的时间
	 */
	public abstract int getShowTime();
	
	public abstract void onShow();
	
	public abstract void onJudgment(Judgment judgment);
	
	public abstract PreciseTimeline getTimeLine();
	
	/**
	 *当前HitObject是否已经结束
	 */
	public abstract boolean isFinished();
}
