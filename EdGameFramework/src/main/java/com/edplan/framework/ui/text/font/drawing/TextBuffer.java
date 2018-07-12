package com.edplan.framework.ui.text.font.drawing;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.ui.text.font.bmfont.FNTChar;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class TextBuffer
{
	private Texture3DBatch[] bactchs;
	private GLTexture[] textures;
	
	public TextBuffer(BMFont font){
		bactchs=new Texture3DBatch[font.getPageCount()];
		textures=new GLTexture[font.getPageCount()];
		for(int i=0;i<textures.length;i++){
			textures[i]=font.getPage(i).texture.getTexture();
		}
	}
	
	public void clear(){
		for(Texture3DBatch b:bactchs){
			if(b!=null)b.clear();
		}
	}
	
	public Texture3DBatch getBatch(int p){
		if(bactchs[p]==null)bactchs[p]=new Texture3DBatch<TextureVertex3D>();
		return bactchs[p];
	}
	
	public void printBlock(TextBlock block,float x,float y,Color4 color){
		final int end=block.chars.length;
		final int[] datas=block.datas;
		for(int i=0,j=0;i<end;i++,j+=4){
			final FNTChar c=block.chars[i];
			TextureVertex3D[] vs=BaseCanvas.rectToTriangles(BaseCanvas.createBaseVertexs(
				c.rawTextureArea,
				RectF.ltrb(
					x+datas[j],y+datas[j+1],x+datas[j+2],y+datas[j+3]
				),
				color,
				0));
			getBatch(c.page).add(vs);
		}
	}
	
	public void printToCanvas(BaseCanvas canvas,float alpha,Color4 mixColor){
		for(int i=0;i<bactchs.length;i++){
			final Texture3DBatch b=bactchs[i];
			if(b!=null){
				canvas.drawTexture3DBatch(b,textures[i],alpha,mixColor);
			}
		}
	}
}
