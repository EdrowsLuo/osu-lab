package com.edplan.nso.ruleset.mania.objects;
import com.edplan.nso.ruleset.std.objects.StdHitCircle;
import com.edplan.nso.ruleset.std.objects.StdHitObject;

public class ManiaNote extends StdHitCircle
{
	public ManiaNote(){
		
	}
	
	public ManiaNote(StdHitCircle hc){
		super();
		setAddition(hc.getAddition());
		//setSampleSetAddition(hc.getSampleSetAddition());
		setStartTime(hc.getStartTime());
		//setCustomIndex(hc.getCustomIndex());
		setAddition(hc.getAddition());
		//setOverrideSampleFile(hc.getOverrideSampleFile());
		setIsNewCombo(hc.isNewCombo());
		setComboColorsSkip(hc.getComboColorsSkip());
		//setSampleSet(hc.getSampleSet());
		setHitSound(hc.getHitSound());
		setStartX(hc.getStartX());
		//setSampleVolume(hc.getSampleVolume());
		setStartY(hc.getStartY());
	}
	
	public static ManiaNote toManiaNote(StdHitObject obj){
		if(obj instanceof ManiaNote){
			return (ManiaNote)obj;
		}else if(obj instanceof StdHitCircle){
			return new ManiaNote((StdHitCircle)obj);
		}else{
			return null;
		}
	}
}
