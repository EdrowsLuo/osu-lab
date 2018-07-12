package com.edplan.framework.ui.text.font.bmfont;

public class FNTCommon
{
	public static final String LINEHEIGHT="lineHeight";
	public static final String BASE="base";
	public static final String SCALEW="scaleW";
	public static final String SCALEH="scaleH";
	public static final String PAGES="pages";
	public static final String PACKED="packed";
	public static final String ALPHACHNL="alphaChnl";
	public static final String REDCHNL="redChnl";
	public static final String GREENCHNL="greenChnl";
	public static final String BLUECHNL="blueChnl";
	
	public final int lineHeight;
	public final int base;
	public final int scaleW;
	public final int scaleH;
	public int pages;
	public final boolean packed;
	public final int alphaChnl;
	public final int redChnl;
	public final int greenChnl;
	public final int blueChnl;
	
	public FNTCommon(
		int lineHeight,
		int base,
		int scaleW,
		int scaleH,
		int pages,
		boolean packed,
		int alphaChnl,
		int redChnl,
		int greenChnl,
		int blueChnl
	){
		this.lineHeight=lineHeight;
		this.base=base;
		this.scaleW=scaleW;
		this.scaleH=scaleH;
		this.pages=pages;
		this.packed=packed;
		this.alphaChnl=alphaChnl;
		this.redChnl=redChnl;
		this.greenChnl=greenChnl;
		this.blueChnl=blueChnl;
	}
	
	public static FNTCommon parse(String line){
		String[] spl=line.split(" ");
		int lineHeight=FNTHelper.parseInt(LINEHEIGHT,1,spl[1]);
		int base=FNTHelper.parseInt(BASE,1,spl[2]);
		int scaleW=FNTHelper.parseInt(SCALEW,1,spl[3]);
		int scaleH=FNTHelper.parseInt(SCALEH,1,spl[4]);
		int pages=FNTHelper.parseInt(PAGES,1,spl[5]);
		boolean packed=1==FNTHelper.parseInt(PACKED,1,spl[6]);
		int alphaChnl=FNTHelper.parseInt(ALPHACHNL,1,spl[7]);
		int redChnl=FNTHelper.parseInt(REDCHNL,1,spl[8]);
		int greenChnl=FNTHelper.parseInt(GREENCHNL,1,spl[9]);
		int blueChnl=FNTHelper.parseInt(BLUECHNL,1,spl[10]);
		return new FNTCommon(
			lineHeight,
			base,
			scaleW,
			scaleH,
			pages,
			packed,
			alphaChnl,
			redChnl,
			greenChnl,
			blueChnl
		);
	}
}
