package com.edplan.nso.beatmapComponent;
import com.edplan.superutils.interfaces.StringMakeable;

public enum SampleSet implements StringMakeable
{
	None("None"),Soft("Soft"),Normal("Normal"),Drum("Drum")
	;
	private final String value;

	SampleSet(String v){
		value=v;
	}

	public String value(){
		return value;
	}

	public static SampleSet parse(String s){
		switch(s){
			case "0":
			case "None":
				return None;
			case "1":
			case "Normal":
				return Normal;
			case "2":
			case "Soft":
				return Soft;
			case "3":
			case "Drum":
				return Drum;
			default:
				return null;
		}
	}

	public static SampleSet fromName(String s){
		switch(s){
			case "None":
				return None;
			case "Normal":
				return Normal;
			case "Soft":
				return Soft;
			case "Drum":
				return Drum;
			default:
				return null;
		}
	}
	
	/*
	public SampleSet(){
		
	}
	
	public SampleSet(Type t){
		setType(t);
	}

	public void setType(Type type){
		this.type=type;
	}

	public Type getType(){
		return type;
	}*/
	
	@Override
	public String makeString(){

		return value; 
	}
}
