package com.edplan.nso.ruleset.mania;
import com.edplan.nso.ruleset.std.StdBeatmap;

public class ManiaBeatmap extends StdBeatmap
{
	public ManiaBeatmap(StdBeatmap res){
		setVersion(res.getVersion());
		setGeneral(res.getGeneral());
		setEditor(res.getEditor());
		setMetadata(res.getMetadata());
		setDifficulty(res.getDifficulty());
		setEvent(res.getEvent());
		setTimingPoints(res.getTimingPoints());
		setColours(res.getColours());
		setHitObjects(res.getHitObjects());
	}
}
