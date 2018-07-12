package com.edplan.nso.ruleset.base.playing;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.nso.filepart.PartDifficulty;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.std.playing.controlpoint.ControlPoints;

public abstract class PlayingBeatmap
{
	private PreciseTimeline timeLine;

	private OsuSkin skin;
	
	public PlayingBeatmap(OsuSkin skin,PreciseTimeline timeline){
		this.skin=skin;
		this.timeLine=timeline;
	}

	public void setSkin(OsuSkin skin) {
		this.skin=skin;
	}

	public OsuSkin getSkin() {
		return skin;
	}
	
	public PreciseTimeline getTimeLine() {
		return timeLine;
	}
	
	public abstract PartDifficulty getDifficulty();
	
	public abstract ControlPoints getControlPoints();
}
