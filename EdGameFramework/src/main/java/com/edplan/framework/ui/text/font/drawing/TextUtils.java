package com.edplan.framework.ui.text.font.drawing;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.text.font.bmfont.FNTChar;
import com.edplan.framework.ui.text.font.bmfont.FNTKerning;
import java.util.ArrayList;

public class TextUtils
{
	public static int calwidth(BMFont f,String text){
		char[] block=new char[text.length()];
		text.getChars(0,text.length(),block,0);
		return calwidth(f,block,0,block.length);
	}
	
	public static int calwidth(final BMFont font,final char[] block,int offset,int length){
		if(length<=0){
			return 0;
		}else if(length==1){
			return font.getFNTCharSafe(block[offset]).xadvance;
		}else{
			int width=font.getFNTCharSafe(block[offset]).xadvance;
			final int end=offset+length;
			char pre=block[offset];
			for(int i=offset+1;i<end;i++){
				final char rc=block[i];
				final FNTChar c=font.getFNTCharSafe(rc);
				width+=c.xadvance+font.getKerningAmount(pre,rc);
				pre=rc;
			}
			return width;
		}
	}
	
	public static int forceBreakText(final BMFont font,final char[] block,int offset,int max){
		if(offset>=block.length)return offset;
		int idx=offset;
		char pre=block[idx];
		if(pre=='\n')return idx;
		int width=font.getFNTChar(block[offset]).xadvance;
		idx++;
		final int end=block.length;
		while(true){
			if(idx>=end)return end;
			final char rc=block[idx];
			if(rc=='\n')return idx;
			final FNTChar c=font.getFNTChar(rc);
			width+=c.xadvance+font.getKerningAmount(pre,rc);
			if(width>=max){
				return idx;
			}
			pre=rc;
			idx++;
		}
	}
	
	public static int breakText(final BMFont font,final char[] block,int offset,int max){
		if(offset>=block.length)return offset;
		int idx=offset;
		char pre=block[idx];
		if(pre=='\n')return idx;
		int breakOffset=offset;
		int width=font.getFNTCharSafe(block[offset]).xadvance;
		idx++;
		int breakLevel=getBreakLevel(pre);
		final int end=block.length;
		while(true){
			if(idx>=end)return end;
			final char rc=block[idx];
			if(rc=='\n')return idx;
			final int l=getBreakLevel(rc);
			if(l>=breakLevel){
				breakOffset=idx;
				breakLevel=l;
			}
			final FNTChar c=font.getFNTCharSafe(rc);
			width+=c.xadvance+font.getKerningAmount(pre,rc);
			if(width>=max){
				return breakOffset;
			}
			pre=rc;
			breakLevel--;
			idx++;
		}
	}
	
	public static TextBlock[] breakTextAsBlock(BMFont font,String text,int max){
		char[] block=new char[text.length()];
		text.getChars(0,text.length(),block,0);
		ArrayList<TextBlock> list=new ArrayList<TextBlock>(2);
		int offset=0;
		final int end=block.length;
		while(true){
			final int o=breakText(font,block,offset,max);
			if(o==end){
				if(o>offset)list.add(new TextBlock(font,block,offset,o-offset));
				break;
			}
			if(o==offset){
				//offset无法前进，可能是到达了换行符
				if(block[o]=='\n'){
					//list.add(new TextBlock());
					offset++;
					continue;
				}else{
					//到达的不是换行符，强行breakText
					final int oo=forceBreakText(font,block,offset,max);
					if(oo==offset){
						//无解，可能是长度太小无法装载下一个字符，直接返回
						TextBlock[] l=new TextBlock[list.size()];
						list.toArray(l);
						return l;
						//throw new RuntimeException("breakText你妹啊，死循环了老哥");
					}else{
						list.add(new TextBlock(font,block,offset,oo-offset/*+((block[oo]=='\n')?-1:0)*/));
						offset=oo;
						continue;
					}
				}
			}else{
				list.add(new TextBlock(font,block,offset,o-offset));
				offset=o;
			}
		}
		TextBlock[] l=new TextBlock[list.size()];
		list.toArray(l);
		return l;
	}
	
	public static String[] breakText(BMFont font,String text,int max){
		char[] block=new char[text.length()];
		text.getChars(0,text.length(),block,0);
		ArrayList<String> list=new ArrayList<String>(2);
		int offset=0;
		final int end=block.length;
		while(true){
			final int o=breakText(font,block,offset,max);
			if(o==end){
				if(o>offset)list.add(text.substring(offset,o));
				break;
			}
			if(o==offset){
				//offset无法前进，可能是到达了换行符
				if(block[o]=='\n'){
					offset++;
					continue;
				}else{
					//到达的不是换行符，强行breakText
					final int oo=forceBreakText(font,block,offset,max);
					if(oo==offset){
						//无解，抛错误
						throw new RuntimeException("breakText你妹啊，死循环了老哥");
					}else{
						list.add(text.substring(offset,oo));
						offset=oo;
						continue;
					}
				}
			}else{
				list.add(text.substring(offset,o));
				offset=o;
			}
		}
		String[] l=new String[list.size()];
		list.toArray(l);
		return l;
	}
	
	public static int getBreakLevel(char c){
		switch(c){
			case '\n':
				return 999999;
			case ' ':
				return 12;
			case '.':
				return 9;
			case '*':
			case '=':
			case '/':
				return 6;
			case '(':
			case '{':
			case '[':
				return-1;
			default:
				 return 0;
		}
	}
}
