package com.edplan.framework.ui.text.font.bmfont;
import java.util.HashMap;
import java.io.InputStream;
import com.edplan.superutils.AdvancedBufferedReader;
import java.io.IOException;

public class BMFontDescription
{
	public FNTInfo info;
	public FNTCommon common;
	public FNTPage[] pages;
	public HashMap<Character,FNTChar> chars=new HashMap<Character,FNTChar>();
	public HashMap<KerningPair,FNTKerning> kernings=new HashMap<KerningPair,FNTKerning>();

	public void setInfo(FNTInfo info) {
		this.info=info;
	}

	public FNTInfo getInfo() {
		return info;
	}

	public void setCommon(FNTCommon common) {
		this.common=common;
	}

	public FNTCommon getCommon() {
		return common;
	}

	public void setPages(FNTPage[] pages) {
		this.pages=pages;
	}

	public FNTPage[] getPages() {
		return pages;
	}

	public void setChars(HashMap<Character, FNTChar> chars) {
		this.chars=chars;
	}

	public HashMap<Character, FNTChar> getChars() {
		return chars;
	}

	public void setKernings(HashMap<KerningPair, FNTKerning> kernings) {
		this.kernings=kernings;
	}

	public HashMap<KerningPair, FNTKerning> getKernings() {
		return kernings;
	}
	
	/**
	 *解析一个包含FNT数据的流
	 */
	public void parse(InputStream data) throws IOException{
		AdvancedBufferedReader reader=new AdvancedBufferedReader(data);
		info=FNTInfo.parse(reader.readLine());
		common=FNTCommon.parse(reader.readLine());
		pages=new FNTPage[common.pages];
		for(int i=0;i<common.pages;i++){
			pages[i]=FNTPage.parse(reader.readLine());
		}
		int charsCount=FNTHelper.parseInt("count",1,reader.readLine().split(" ")[1]);
		for(int i=0;i<charsCount;i++){
			FNTChar ch=FNTChar.parse(reader.readLine());
			chars.put(ch.id,ch);
		}
		
		//部分字体可能没有Kernings
		String line;
		if((line=reader.readLine())!=null){
			int kerningCount=FNTHelper.parseInt("count",1,line.split(" ")[1]);
			for(int i=0;i<kerningCount;i++){
				FNTKerning kerning=FNTKerning.parse(reader.readLine());
				kernings.put(new KerningPair(kerning.first,kerning.second),kerning);
			}
		}
	}
	
	public static BMFontDescription fromStream(InputStream in) throws IOException{
		BMFontDescription d=new BMFontDescription();
		d.parse(in);
		return d;
	}
}
