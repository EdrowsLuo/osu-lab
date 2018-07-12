package com.edplan.framework.ui.text.font.bmfont;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public class FNTPage
{
	public static final String ID="id";
	public static final String FILE="file";
	
	public final int id;
	public final String file;
	
	
	public FNTPage(int id,String file){
		this.id=id;
		this.file=file;
	}
	
	public static FNTPage parse(String line){
		String[] spl=line.split(" ");
		int id=FNTHelper.parseInt(ID,1,spl[1]);
		String file=FNTHelper.parseString(FILE,1,spl[2]);
		return new FNTPage(id,file);
	}
}
