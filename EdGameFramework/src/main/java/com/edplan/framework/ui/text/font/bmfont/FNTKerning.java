package com.edplan.framework.ui.text.font.bmfont;

public class FNTKerning
{
	public static final String FIRST="first";
	public static final String SECOND="second";
	public static final String AMOUNT="amount";
	
	public final char first;
	public final char second;
	public final int amount;
	
	public FNTKerning(char first,char second,int amount){
		this.first=first;
		this.second=second;
		this.amount=amount;
	}
	
	public static FNTKerning parse(String fntline){
		String[] split=FNTHelper.removeEmpty(fntline.split(" "));
		if(split.length!=4){
			throw new IllegalArgumentException("a kerning line must has 4 parts : "+fntline);
		}
		char first=(char)FNTHelper.parseInt(FIRST,1,split[1]);
		char second=(char)FNTHelper.parseInt(SECOND,1,split[2]);
		int amount=FNTHelper.parseInt(AMOUNT,1,split[3]);
		return new FNTKerning(
			first,second,amount
		);
	}
}
