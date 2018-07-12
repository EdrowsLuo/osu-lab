package com.edplan.framework.ui.text.font;
import java.util.ArrayList;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class DrawString
{
	private TextureCharacterPool pool;
	
	private String text="";
	
	private float height=100;
	
	private CharacterInfo unknownCharacterReplacer;
	
	private float textDeltaX=0;
	
	private float whiteSpaceWidth=10;
	
	private ArrayList<DrawStringNode> mesuredNode;

	private boolean needUpdate=true;

	public DrawString(String text,TextureCharacterPool pool,float height){
		this.text=text;
		this.pool=pool;
		this.height=height;
	}
	
	public void postUpdate(){
		needUpdate=true;
	}

	public void setPool(TextureCharacterPool pool) {
		postUpdate();
		this.pool=pool;
	}

	public TextureCharacterPool getPool() {
		return pool;
	}

	public void setText(String text) {
		if(this.text.equals(text))return;
		postUpdate();
		this.text=text;
	}

	public String getText() {
		return text;
	}

	public void setHeight(float height) {
		if(this.height==height)return;
		postUpdate();
		this.height=height;
	}

	public float getHeight() {
		return height;
	}

	public void setUnknownCharacterReplacer(CharacterInfo unknownCharacterReplacer) {
		if(this.unknownCharacterReplacer==unknownCharacterReplacer)return;
		postUpdate();
		this.unknownCharacterReplacer=unknownCharacterReplacer;
	}

	public CharacterInfo getUnknownCharacterReplacer() {
		return unknownCharacterReplacer;
	}

	public void setTextDeltaX(float textDeltaX) {
		if(this.textDeltaX==textDeltaX)return;
		postUpdate();
		this.textDeltaX=textDeltaX;
	}

	public float getTextDeltaX() {
		return textDeltaX;
	}

	public void setWhiteSpaceWidth(float whiteSpaceWidth) {
		if(this.whiteSpaceWidth==whiteSpaceWidth)return;
		postUpdate();
		this.whiteSpaceWidth=whiteSpaceWidth;
	}

	public float getWhiteSpaceWidth() {
		return whiteSpaceWidth;
	}
	
	public void update(){
		mesuredNode=new ArrayList<DrawStringNode>(text.length());
		float currentX=0;
		DrawStringNode tmp;
		char c;
		CharacterInfo info;
		for(int i=0;i<text.length();i++){
			c=text.charAt(i);
			if(c==' '){
				currentX+=whiteSpaceWidth+textDeltaX;
			}else{
				tmp=new DrawStringNode();
				if(pool.has(c)){
					info=pool.get(c);
				}else{
					if(unknownCharacterReplacer!=null){
						info=unknownCharacterReplacer;
					}else{
						currentX+=whiteSpaceWidth+textDeltaX;
						continue;
					}
				}
				if(info.getWidth()==1||info.getHeight()==1){
					currentX+=whiteSpaceWidth+textDeltaX;
				}
				tmp.setInfo(info);
				tmp.setDrawBaseX(currentX);
				tmp.setDrawWidth(info.getWidth()*height/info.getHeight());
				tmp.setDrawHeight(height);
				currentX+=tmp.getDrawWidth()+textDeltaX;
				mesuredNode.add(tmp);
			}
		}
		needUpdate=false;
	}
	
	public void checkForDraw(){
		if(needUpdate){
			update();
		}
	}
	
	/**
	 *暂时只做绘制一行的
	 */
	public void drawToCanvas(BaseCanvas canvas,GLPaint paint,Vec2 basePoint){
		checkForDraw();
		for(DrawStringNode n:mesuredNode){
			canvas.drawTexture(n.getInfo().getTexture(),makeNodeArea(n,basePoint),paint);
		}
	}
	
	public void drawToCanvas(BaseCanvas canvas,GLPaint paint,Vec2 basePoint,Alignment alig){
		checkForDraw();
		float xoffset=0;
		float yoffset=0;
		DrawStringNode endNode;
		switch(alig){
			case Left:
				xoffset=0;
				break;
			case Right:
				endNode=mesuredNode.get(mesuredNode.size()-1);
				xoffset=-endNode.getDrawBaseX()-endNode.getDrawWidth();
				break;
			case Center:
				endNode=mesuredNode.get(mesuredNode.size()-1);
				xoffset=-(endNode.getDrawBaseX()+endNode.getDrawWidth())/2;
				yoffset=-height/2;
				break;
			default:break;
		}
		drawToCanvas(canvas,paint,new Vec2(basePoint.x+xoffset,basePoint.y+yoffset));
	}
	
	private RectF makeNodeArea(DrawStringNode node,Vec2 basePoint){
		return new RectF(basePoint.x+node.getDrawBaseX(),basePoint.y,node.getDrawWidth(),node.getDrawHeight());
	}
	
	public enum Alignment{
		Left,
		Right,
		Center;
	}
}
