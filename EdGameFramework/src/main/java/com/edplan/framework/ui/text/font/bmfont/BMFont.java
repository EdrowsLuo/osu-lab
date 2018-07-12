package com.edplan.framework.ui.text.font.bmfont;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.ui.text.font.bmfont.BMFontDescription;
import com.edplan.framework.ui.text.font.bmfont.FNTPage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.edplan.framework.ui.looper.ExpensiveTask;
import com.edplan.framework.MContext;

/**
 *单个字体，包含所有face相同的字体文件
 */
public class BMFont
{
	public static final String Noto_Sans_CJK_JP_Medium="Noto Sans CJK JP Medium";
	
	public static final String FontAwesome="FontAwesome";
	
	public static final String Exo_20="Exo 2.0";
	
	public static final String Exo_20_Semi_Bold="Exo 2.0 Semi Bold";
	
	private static HashMap<String,BMFont> fonts=new HashMap<String,BMFont>();

	private static BMFont defaultFont;
	
	public static char CHAR_NOT_FOUND=8709;
	
	public static void setDefaultFont(BMFont defaultFont){
		BMFont.defaultFont=defaultFont;
	}

	public static BMFont getDefaultFont(){
		return defaultFont;
	}

	public static void addFont(BMFont font,String name){
		fonts.put(name,font);
	}
	
	public static BMFont getFont(String name){
		return fonts.get(name);
	}
	
	private String face;
	
	private FNTInfo info;
	
	private FNTCommon common;

	private ArrayList<LoadedPage> pages=new ArrayList<LoadedPage>();

	private HashMap<Character,FNTChar> charmap=new HashMap<Character,FNTChar>();

	private HashMap<KerningPair,FNTKerning> kerningmap=new HashMap<KerningPair,FNTKerning>();

	private FNTChar errCharacter;


	private MContext context;

	protected BMFont(MContext context){
		this.context=context;
	}

	public void setErrCharacter(char c) {
		this.errCharacter=getFNTChar(c);
	}

	public FNTChar getErrCharacter() {
		return errCharacter;
	}

	public FNTInfo getInfo() {
		return info;
	}

	public FNTCommon getCommon() {
		return common;
	}
	
	public FNTChar getFNTChar(char c){
		return charmap.get(c);
	}
	
	public FNTChar getFNTCharSafe(char c){
		final FNTChar cc=charmap.get(c);
		if(cc!=null){
			return cc;
		}else{
			return errCharacter;
		}
	}
	
	public FNTKerning getKerning(char first,char second){
		return kerningmap.get(new KerningPair(first,second));
	}
	
	public int getKerningAmount(char first,char second){
		final FNTKerning k=getKerning(first,second);
		return (k!=null)?k.amount:0;
	}
	
	public LoadedPage getPage(int page){
		return pages.get(page);
	}
	
	public int getPageCount(){
		return pages.size();
	}
	
	public void addFont(AResource res,String fontFile){
		try {
			addFont(res, BMFontDescription.fromStream(res.openInput(fontFile)));
		} catch (IOException e) {
			throw new BMFontException("err io: "+e.getMessage(),e);
		}
	}
	
	public void addFont(AResource res,BMFontDescription description){
		/*if(!face.equals(description.getInfo().face)){
			throw new BMFontException("only font with the same face can be added"); 
		}*/
		int pageOffset=pages.size();
		for(FNTPage page:description.getPages()){
			LoadedPage loadedPage=new LoadedPage();
			loadedPage.id=pageOffset+page.id;
			try {
				loadedPage.texture=
				//ExpensiveTask.loadTextureSync(context,res,page.file);
				res.loadTexture(page.file);
				pages.add(loadedPage);
			} catch (IOException e) {
				throw new BMFontException("err io: "+e.getMessage(),e);
			}
		}
		for(FNTChar c:description.chars.values()){
			c.page+=pageOffset;
			c.tobase=description.getCommon().base-c.yoffset;
			c.rawTextureArea=pages
								 .get(c.page)
								  .texture
								   .toTextureRect(
								   	c.x,c.y,c.x+c.width,c.y+c.height
								   );
			charmap.put(c.id,c);
		}
		for(Map.Entry<KerningPair,FNTKerning> e:description.kernings.entrySet()){
			kerningmap.put(e.getKey(),e.getValue());
		}
	}
	
	protected void initialFont(BMFontDescription desc){
		info=desc.getInfo();
		common=desc.getCommon();
		face=info.face;
	}
	
	public static BMFont loadFont(MContext c,AResource res,String fontFile) throws IOException{
		return loadFont(c,res,fontFile,CHAR_NOT_FOUND);
	}

	/**
	 *
	 */
	public static BMFont loadFont(MContext c,AResource res,String fontFile,char errCharId) throws IOException{
		BMFont font=new BMFont(c);
		BMFontDescription desc=BMFontDescription.fromStream(res.openInput(fontFile));
		font.initialFont(desc);
		font.addFont(res,desc);
		font.errCharacter=font.getFNTChar(errCharId);
		return font;
	}
	
	public class FontType{
		public boolean bold=false;
		public boolean italic=false;
		
		public FontType(boolean bold,boolean italic){
			this.bold=bold;
			this.italic=italic;
		}

		@Override
		public int hashCode() {

			return (bold?1:0)|(italic?2:0);
		}

		@Override
		public boolean equals(Object obj) {

			if(obj instanceof FontType){
				FontType other=(FontType)obj;
				return other.bold==bold&&other.italic==italic;
			}else{
				throw new RuntimeException("you can only call equal with 2 FontType");
			}
		}
	}
	
	public class FontEntry{
		protected ArrayList<LoadedPage> pages=new ArrayList<LoadedPage>();

		protected HashMap<Character,FNTChar> charmap=new HashMap<Character,FNTChar>();

		protected HashMap<KerningPair,FNTKerning> kerningmap=new HashMap<KerningPair,FNTKerning>();
	}
}
