package com.edplan.nso.ruleset.std.objects;

public class HitObjectAddition
{
	private int sampleSet;
	private int sampleAddition;
	private int customIndex;
	private int sampleVolume;
	private String overrideSampleFile;
	
	public boolean hasOverrideSampleFile(){
		return overrideSampleFile!=null;
	}

	public void setSampleSet(int sampleSet){
		this.sampleSet=sampleSet;
	}

	public int getSampleSet(){
		return sampleSet;
	}

	public void setSampleAddition(int sampleAddition){
		this.sampleAddition=sampleAddition;
	}

	public int getSampleAddition(){
		return sampleAddition;
	}

	public void setCustomIndex(int customIndex){
		this.customIndex=customIndex;
	}

	public int getCustomIndex(){
		return customIndex;
	}

	public void setSampleVolume(int sampleVolume){
		this.sampleVolume=sampleVolume;
	}

	public int getSampleVolume(){
		return sampleVolume;
	}

	public void setOverrideSampleFile(String overrideSampleFile){
		this.overrideSampleFile=overrideSampleFile;
	}

	public String getOverrideSampleFile(){
		return overrideSampleFile;
	}
}
