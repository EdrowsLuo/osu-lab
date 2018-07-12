package com.edplan.framework.ui.layout;
import java.util.HashMap;

/** 
 *描述组件相对位置
 */
public class Gravity
{
	public static final int VERTICAL_LEFT_BITS=4;
	
	public static final int MASK_HORIZON=(1<<VERTICAL_LEFT_BITS)-1;
	
	public static final int MASK_VERTICAL=MASK_HORIZON<<VERTICAL_LEFT_BITS;
	
	public static final int LEFT=1;
	
	public static final int CENTER_HORIZON=2;
	
	public static final int RIGHT=3;
	
	public static final int TOP=1<<VERTICAL_LEFT_BITS;
	
	public static final int CENTER_VERTICAL=2<<VERTICAL_LEFT_BITS;
	
	public static final int BOTTOM=3<<VERTICAL_LEFT_BITS;
	
	public static final String SNone="None";
	
	public static final String STopLeft="TopLeft";
	
	public static final String STopCenter="TopCenter";
	
	public static final String STopRight="TopRight";
	
	public static final String SCenterLeft="CenterLeft";
	
	public static final String SCenter="Center";
	
	public static final String SCenterRight="CenterRight";
	
	public static final String SBottomLeft="BottomLeft";
	
	public static final String SBottomCenter="BottomCenter";
	
	public static final String SBottomRight="BottomRight";
	
	public static final String STop="Top";
	
	public static final String SCenterVertical="CenterVertical";
	
	public static final String SBottom="Bottom";
	
	public static final String SLeft="Left";
	
	public static final String SCenterHorizon="CenterHorizon";
	
	public static final String SRight="Right";
	
	public static final int None=0;
	
	public static final int TopLeft=TOP|LEFT;
	
	public static final int TopCenter=TOP|CENTER_HORIZON;
	
	public static final int TopRight=TOP|RIGHT;
	
	public static final int CenterLeft=CENTER_VERTICAL|LEFT;
	
	public static final int Center=CENTER_VERTICAL|CENTER_HORIZON;
	
	public static final int CenterRight=CENTER_VERTICAL|RIGHT;
	
	public static final int BottomLeft=BOTTOM|LEFT;
	
	public static final int BottomCenter=BOTTOM|CENTER_HORIZON;
	
	public static final int BottomRight=BOTTOM|RIGHT;
	
	public static final HashMap<String,Integer> parseMap;
	
	static{
		parseMap=new HashMap<String,Integer>();
		parseMap.put(STopLeft,TopLeft);
		parseMap.put(STopCenter,TopCenter);
		parseMap.put(STopRight,TopRight);
		parseMap.put(SCenterLeft,CenterLeft);
		parseMap.put(SCenter,Center);
		parseMap.put(SCenterRight,CenterRight);
		parseMap.put(SBottomLeft,BottomLeft);
		parseMap.put(SBottomCenter,BottomCenter);
		parseMap.put(SBottomRight,BottomRight);
		parseMap.put(STop,TOP);
		parseMap.put(SCenterVertical,CENTER_VERTICAL);
		parseMap.put(SBottom,BOTTOM);
		parseMap.put(SLeft,LEFT);
		parseMap.put(SCenterHorizon,CENTER_HORIZON);
		parseMap.put(SRight,RIGHT);
	}
	
	public static int parse(String res){
		int r=0;
		String[] ss=res.split("\\|");
		for(String s:ss){
			r|=parseMap.get(s);
		}
		return r;
	}
}
