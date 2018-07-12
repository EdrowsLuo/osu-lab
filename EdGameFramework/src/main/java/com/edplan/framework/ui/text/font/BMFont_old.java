package com.edplan.framework.ui.text.font;
import com.edplan.framework.ui.text.font.bmfont.BMFontDescription;
import com.edplan.framework.resource.AResource;
import java.util.ArrayList;
import com.edplan.framework.ui.text.font.bmfont.FNTPage;

/**
 *单个字体，包含所有face相同的字体文件
 */
public class BMFont_old
{
	private BMFontDescription description=new BMFontDescription();
	
	private String face;
	
	private ArrayList<FNTPage> pages;
	
	BMFont_old(){
		
	}
	
	/**
	 *
	 */
	public static void loadFont(AResource res,String fontName){
		
	}
	
	
	
}
