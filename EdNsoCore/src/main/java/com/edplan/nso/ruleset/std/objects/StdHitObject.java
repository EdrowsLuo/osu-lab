package com.edplan.nso.ruleset.std.objects;
import com.edplan.nso.ruleset.base.object.HitObject;

public abstract class StdHitObject extends HitObject
{
	private int startX;
	private int startY;
	private int startTime;
	private boolean isNewCombo;
	private int comboColorsSkip;
	private int hitSound;
	private HitObjectAddition addition;
	
	public abstract StdHitObjectType getResType();
	
	public void setAddition(HitObjectAddition addition){
		this.addition=addition;
	}

	public HitObjectAddition getAddition(){
		return addition;
	}

	public void setIsNewCombo(boolean isNewCombo){
		this.isNewCombo=isNewCombo;
	}

	public boolean isNewCombo(){
		return isNewCombo;
	}

	public void setComboColorsSkip(int comboColorsSkip){
		this.comboColorsSkip=comboColorsSkip;
	}

	public int getComboColorsSkip(){
		return comboColorsSkip;
	}

	public void setHitSound(int hitSound){
		this.hitSound=hitSound;
	}

	public int getHitSound(){
		return hitSound;
	}

	public void setSampleSet(int sampleSet){
		this.getAddition().setSampleSet(sampleSet);
	}

	public int getSampleSet(){
		return getAddition().getSampleSet();
	}

	public void setSampleSetAddition(int addition){
		this.getAddition().setSampleAddition(addition);
	}

	public int getSampleSetAddition(){
		return getAddition().getSampleAddition();
	}

	public void setCustomIndex(int customIndex){
		this.getAddition().setCustomIndex(customIndex);
	}

	public int getCustomIndex(){
		return getAddition().getCustomIndex();
	}

	public void setSampleVolume(int sampleVolume){
		this.getAddition().setSampleVolume(sampleVolume);
	}

	public int getSampleVolume(){
		return getAddition().getSampleVolume();
	}

	public void setOverrideSampleFile(String overrideSampleFile){
		this.getAddition().setOverrideSampleFile(overrideSampleFile);
	}

	public String getOverrideSampleFile(){
		return getAddition().getOverrideSampleFile();
	}
	
	public boolean hasOverrideSampleFile(){
		return getOverrideSampleFile()!=null&&getOverrideSampleFile().trim().length()>0;
	}

	public void setStartX(int startX){
		this.startX=startX;
	}

	public int getStartX(){
		return startX;
	}

	public void setStartY(int startY){
		this.startY=startY;
	}

	public int getStartY(){
		return startY;
	}

	public void setStartTime(int time){
		this.startTime=time;
	}

	public int getStartTime(){
		return startTime;
	}
	
	/*
	public void setEndTime(int time){
		startTime=time;
	}
	
	public int getEndTime(){
		return startTime;
	}*/
}
