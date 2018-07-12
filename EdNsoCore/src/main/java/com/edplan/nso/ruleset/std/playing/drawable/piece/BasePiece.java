package com.edplan.nso.ruleset.std.playing.drawable.piece;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.framework.ui.drawable.interfaces.IFadeable;
import com.edplan.framework.ui.drawable.interfaces.IScaleable2D;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.framework.utils.MLog;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public abstract class BasePiece extends EdDrawable implements IScaleable2D,IFadeable
{
	public static float DEF_SIZE=64;
	
	private float baseSize=DEF_SIZE;

	private Vec2 origin=new Vec2();

	private Vec2 scale=new Vec2(1, 1);

	protected GLPaint paint=new GLPaint();
	
	protected GLPaint tmpPaint;

	private PreciseTimeline timeline;
	
	private Color4 accentColor=Color4.rgba(1,1,1,1);
	
	private boolean finished=false;

	public BasePiece(MContext c,PreciseTimeline timeline){
		super(c);
		this.timeline=timeline;
	}
	
	public void onFinish(){
		
	}

	public void setTimeline(PreciseTimeline timeline) {
		this.timeline=timeline;
	}

	public PreciseTimeline getTimeline() {
		return timeline;
	}

	public void finish() {
		this.finished=true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setAccentColor(Color4 accentColor) {
		this.accentColor.set(accentColor);
	}

	public Color4 getAccentColor() {
		return accentColor;
	}
	
	public void setSkin(OsuSkin skin){
		
	}

	public void setOrigin(Vec2 origin) {
		this.origin.set(origin);
	}

	public Vec2 getOrigin() {
		return origin;
	}

	public void setBaseSize(float baseSize) {
		this.baseSize=baseSize;
	}

	public float getBaseSize() {
		return baseSize;
	}

	@Override
	public void setAlpha(float a) {

		paint.setFinalAlpha(a);
	}

	@Override
	public float getAlpha() {

		return paint.getFinalAlpha();
	}

	@Override
	public void setScale(float sx,float sy) {

		scale.set(sx,sy);
	}

	@Override
	public Vec2 getScale() {

		return scale;
	}
	
	protected void simpleDraw(GLTexture t,BaseCanvas c){
		c.drawTextureAnchorCenter(t,getOrigin(),(new Vec2(getBaseSize(),getBaseSize())).multiple(getScale()),paint);
	}
	
	protected void simpleDrawWithAccentColor(GLTexture t,BaseCanvas c){
		if(tmpPaint==null)tmpPaint=new GLPaint();
		tmpPaint.set(paint);
		tmpPaint.setMixColor(tmpPaint.getMixColor().copyNew().multiple(getAccentColor()));
		c.drawTextureAnchorCenter(t,getOrigin(),(new Vec2(getBaseSize(),getBaseSize())).multiple(getScale()),tmpPaint);
	}
}
