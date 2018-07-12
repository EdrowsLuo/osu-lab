package com.edplan.framework.ui.text.font.drawing;
import java.util.ArrayList;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.text.font.bmfont.FNTChar;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.text.font.bmfont.FNTKerning;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.graphics.opengl.objs.advanced.Texture3DRect;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import android.util.Log;
import java.util.HashMap;
import com.edplan.framework.graphics.opengl.GL10Canvas2D;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class TextPrinter
{
	public static final char NO_PREVIOUS_CHAR=0;
	/**
	 *@field textSize:实际绘制时的大小
	 *@field currentBaseY:当前绘制的起始Y坐标
	 *@field currentX:当前绘制的起始X
	 *@field scale:缩放，设置textSize时自动计算
	 */
	private float textSize;
	
	private float lineHeight;
	
	private float currentBaseY;
	
	private float currentX;
	
	private float startX;
	
	private float startY;
	
	private float scale;
	
	private GLPaint paint;
	
	private ArrayList<Texture3DBatch<TextureVertex3D>> batchs;
	
	private BufferedTextRectGroup rectGroup;
	
	private char preChar=0;
	
	private BMFont font;
	
	private boolean singleline=false;
	
	public TextPrinter(BMFont font,float startX,float startY,GLPaint paint){
		this.font=font;
		this.paint=paint;
		this.startX=startX;
		this.startY=startY;
		initial(startX,startY);
	}
	
	public TextPrinter(String font,float startX,float startY,GLPaint paint){
		this(BMFont.getFont(font),startX,startY,paint);
	}
	
	public TextPrinter(float startX,float startY){
		this(BMFont.getDefaultFont(),startX,startX,new GLPaint());
	}

	public void setSingleline(boolean singleline){
		this.singleline=singleline;
	}

	public boolean isSingleline(){
		return singleline;
	}

	public void setCurrentBaseY(float currentBaseY){
		this.currentBaseY=currentBaseY;
	}

	public float getCurrentBaseY(){
		return currentBaseY;
	}

	public void setCurrentX(float currentX){
		this.currentX=currentX;
	}

	public float getCurrentX(){
		return currentX;
	}

	
	
	public void initial(float startX,float startY){
		batchs=new ArrayList<Texture3DBatch<TextureVertex3D>>();
		for(int i=0;i<font.getPageCount();i++){
			batchs.add(new Texture3DBatch<TextureVertex3D>());
		}
		this.lineHeight=font.getCommon().lineHeight;
		recalScale();
		currentX=startX;
		currentBaseY=startY;
		setTextSize(lineHeight);
	}
	
	private void recalScale(){
		scale=textSize/lineHeight;
	}
	
	public float getScale(){
		return scale;
	}

	public void setTextSize(float s){
		textSize=s;
		recalScale();
	}
	
	public void printString(String str){
		for(int i=0;i<str.length();i++){
			printChar(str.charAt(i));
		}
	}
	
	public void printChars(char... cs){
		for(char c:cs){
			printChar(c);
		}
	}
	
	public void toNextLine(){
		currentX=startX;
		currentBaseY+=textSize;
	}
	
	//移动当前光标
	public void addOffset(float x,float y){
		currentX+=x;
		currentBaseY+=y;
	}
	
	public void addOffsetRaw(float x,float y){
		currentX+=x*scale;
		currentBaseY+=y*scale;
	}
	
	public void printLineCenter(String text,float cx){
		currentX=cx-TextUtils.calwidth(font,text)*scale/2;
		printString(text);
	}
	
	public void printLineLeft(String text,float x){
		currentX=x;
		printString(text);
	}
	
	public void printLineRight(String text,float r){
		currentX=r-TextUtils.calwidth(font,text)*scale;
		printString(text);
	}
	
	public void printChar(char c){
		if((!singleline)&&c=='\n'){
			toNextLine();
			return;
		}
		if(c=='	'){
			printString("    ");
			return;
		}
		FNTChar fntc=font.getFNTChar(c);
		if(fntc!=null){
			RectF area=calCharArea(fntc);
			float xadvance=fntc.xadvance;
			if(preChar!=NO_PREVIOUS_CHAR){
				FNTKerning kerning=font.getKerning(preChar,c);
				if(kerning!=null){
					//area.move(kerning.amount,0);
					//xadvance+=kerning.amount;
				}
			}
			xadvance*=scale;
			Texture3DBatch<TextureVertex3D> batch=getBatchByPage(fntc.page);
			batch.add(Texture3DRect.makeup(area,fntc.rawTextureArea,paint));
			preChar=c;
			currentX+=xadvance;
		}else{
			printErrCharacter();
		}
	}
	
	private Texture3DBatch<TextureVertex3D> getBatchByPage(int page){
		return batchs.get(page);
	}
	
	private RectF calCharArea(FNTChar fntc){
		float x=currentX+fntc.xoffset*scale;
		float y=currentBaseY-fntc.tobase*scale;
		RectF area=RectF.xywh(x,y,fntc.width*scale,fntc.height*scale);
		//y方向的offset和绘制坐标系的方向相反 
		//area.move(fntc.xoffset*scale,-fntc.yoffset*scale*0);
		return area;
	}
	
	public void printErrCharacter(){
		FNTChar fntc=font.getErrCharacter();
		if(fntc==null){
			fntc=font.getFNTChar(' ');
		}
		RectF area=calCharArea(fntc);
		float xadvance=fntc.xadvance;
		xadvance*=scale;
		Texture3DBatch<TextureVertex3D> batch=getBatchByPage(fntc.page);
		batch.add(Texture3DRect.makeup(area,fntc.rawTextureArea,paint));
		preChar=NO_PREVIOUS_CHAR;
		currentX+=xadvance;
	}
	
	public void draw(BaseCanvas canvas){
		for(int i=0;i<font.getPageCount();i++){
			Texture3DBatch<TextureVertex3D> batch=batchs.get(i);
			AbstractTexture texture=font.getPage(i).texture;
			if(batch.getVertexCount()>0){
				canvas.drawTexture3DBatch(batch,texture,paint.getFinalAlpha(),paint.getMixColor());
				//Log.v("text-test","post "+batch.getVertexCount()+" vertex to canvas");
			}
		}
	}
	
	public void drawGL10(GL10Canvas2D canvas){
		for(int i=0;i<font.getPageCount();i++){
			Texture3DBatch<TextureVertex3D> batch=batchs.get(i);
			AbstractTexture texture=font.getPage(i).texture;
			if(batch.getVertexCount()>0){
				canvas.drawTexture3DBatch(batch,texture,paint.getFinalAlpha(),paint.getMixColor());
				//Log.v("text-test","post "+batch.getVertexCount()+" vertex to canvas");
			}
		}
	}
	
}
