package com.edplan.nso.ruleset.std;
import com.edplan.nso.NsoBeatmap;
import com.edplan.nso.filepart.PartColours;
import com.edplan.nso.filepart.PartDifficulty;
import com.edplan.nso.filepart.PartEditor;
import com.edplan.nso.filepart.PartEvents;
import com.edplan.nso.filepart.PartGeneral;
import com.edplan.nso.filepart.PartHitObjects;
import com.edplan.nso.filepart.PartMetadata;
import com.edplan.nso.filepart.PartTimingPoints;
import com.edplan.nso.ruleset.std.objects.StdHitObjects;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.objects.StdSpinner;
import com.edplan.nso.ruleset.std.objects.StdHitCircle;
import com.edplan.nso.ruleset.std.objects.StdSlider;

public class StdBeatmap extends NsoBeatmap
{
	public static int SUPPORT_VERSION=14;

	private int version;
	private PartGeneral general;
	private PartEditor editor;
	private PartMetadata metadata;
	private PartDifficulty difficulty;
	private PartEvents event;
	private PartTimingPoints timingPoints;
	private PartColours colours;
	private StdHitObjects<StdHitObject> hitObjects;

	public StdBeatmap(){
		
	}

	public void setHitObjects(StdHitObjects<StdHitObject> hitObjects){
		this.hitObjects=hitObjects;
	}

	public StdHitObjects<StdHitObject> getHitObjects(){
		return hitObjects;
	}
	
	public void setVersion(int version){
		this.version=version;
	}

	public int getVersion(){
		return version;
	}

	public void setGeneral(PartGeneral general){
		this.general=general;
	}

	public PartGeneral getGeneral(){
		return general;
	}

	public void setEditor(PartEditor editor){
		this.editor=editor;
	}

	public PartEditor getEditor(){
		return editor;
	}

	public void setMetadata(PartMetadata metadata){
		this.metadata=metadata;
	}

	public PartMetadata getMetadata(){
		return metadata;
	}

	public void setDifficulty(PartDifficulty difficulty){
		this.difficulty=difficulty;
	}

	public PartDifficulty getDifficulty(){
		return difficulty;
	}

	public void setEvent(PartEvents event){
		this.event=event;
	}

	public PartEvents getEvent(){
		return event;
	}

	public void setTimingPoints(PartTimingPoints timingPoints){
		this.timingPoints=timingPoints;
	}

	public PartTimingPoints getTimingPoints(){
		return timingPoints;
	}

	public void setColours(PartColours colours){
		this.colours=colours;
	}

	public PartColours getColours(){
		return colours;
	}
	
	
}
