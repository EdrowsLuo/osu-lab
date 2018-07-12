package com.edplan.framework.ui.text.font.drawing;
import com.edplan.framework.ui.text.font.bmfont.FNTChar;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
/**
 *对应一个文字块
 */
public class TextBlock
{
	//datas:存放对应的数据，以ltrb为一组
	public final int[] datas;
	public final FNTChar[] chars;
	
	public final int width;
	
	public TextBlock(){
		datas=new int[0];
		chars=new FNTChar[0];
		width=0;
	}
	
	public TextBlock(BMFont font,char[] block,int offset,int length){
		chars=new FNTChar[length];
		datas=new int[length*4];
		int datapointer=0;
		int x=0;
		char pr='─';
		for(int i=0;i<length;i++){
			final char c=block[i+offset];
			final FNTChar fc=font.getFNTChar(c);
			if(fc==null){
				final FNTChar fcc=font.getErrCharacter();
				chars[i]=fcc;
				datas[datapointer++]=x+fcc.xoffset;
				datas[datapointer++]=fcc.yoffset;
				datas[datapointer++]=x+fcc.xadded;
				datas[datapointer++]=fcc.yadded;
				x+=fc.xadvance;
			}else{
				chars[i]=fc;
				datas[datapointer++]=x+fc.xoffset;
				datas[datapointer++]=fc.yoffset;
				datas[datapointer++]=x+fc.xadded;
				datas[datapointer++]=fc.yadded;
				x+=fc.xadvance+font.getKerningAmount(pr,c);
			}
			pr=c;
		}
		width=x;
	}
	
	public FNTChar charAt(int i){
		return chars[i];
	}
	
	public float getLeft(int idx){
		return datas[idx*4];
	}
	
	public float getTop(int idx){
		return datas[idx*4+1];
	}
	
	public float getRight(int idx){
		return datas[idx*4+2];
	}

	public float getBottom(int idx){
		return datas[idx*4+3];
	}
}
