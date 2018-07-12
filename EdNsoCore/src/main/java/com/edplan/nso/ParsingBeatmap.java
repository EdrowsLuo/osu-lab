package com.edplan.nso;

public class ParsingBeatmap
{
	private String resInfo="unknow res";
	
	private int parsingLine=0;

	public ParsingBeatmap setResInfo(String resInfo){
		this.resInfo=resInfo;
		return this;
	}
	
	public void nextLine(){
		parsingLine++;
	}

	public String getResInfo(){
		return resInfo;
	}
	
	public int getParsingLineIndex(){
		return parsingLine;
	}
}
