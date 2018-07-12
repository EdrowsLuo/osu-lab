package com.edplan.framework.graphics.opengl;

import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.BaseColorBatch;
import com.edplan.framework.graphics.opengl.batch.RectVertexBatch;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.batch.interfaces.ITexture3DBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.objs.Vertex3D;
import com.edplan.framework.graphics.opengl.objs.vertex.RectVertex;
import com.edplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edplan.framework.graphics.opengl.shader.advance.RectTextureShader;
import com.edplan.framework.graphics.opengl.shader.advance.RoundedRectTextureShader;
import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.math.Vec4;
import com.edplan.framework.utils.AbstractSRable;
import java.util.Arrays;
import com.edplan.framework.media.video.tbv.element.DataDrawBaseTexture;

public abstract class BaseCanvas extends AbstractSRable<CanvasData>
{
	private float defZ=0;

	private ITexture3DBatch<TextureVertex3D> tmpBatch;

	private RectVertexBatch<RectVertex> tmpRectBatch;

	private BaseColorBatch<Vertex3D> tmpColorBatch;

	/**
	 *对于开启了post的应该手动post
	 */
	private boolean enablePost=false;
	
	private int maxBatchSize=5000;
	
	private int drawCalls=0;
	
	public BaseCanvas(){
		initialBatch();
	}
	
	public BaseCanvas(boolean initial){
		if(initial)initialBatch();
	}
	
	protected void initialBatch(){
		tmpBatch=createTexture3DBatch();
		tmpRectBatch=createRectVertexBatch();
		tmpColorBatch=createColorBatch();
	}

	public void setMaxBatchSize(int maxBatchSize) {
		this.maxBatchSize=maxBatchSize;
	}

	public int getMaxBatchSize() {
		return maxBatchSize;
	}
	
	protected ITexture3DBatch<TextureVertex3D> createTexture3DBatch(){
		return new Texture3DBatch<TextureVertex3D>();
		//return new DataDrawBaseTexture(6);
	}
	
	protected RectVertexBatch<RectVertex> createRectVertexBatch(){
		return new RectVertexBatch<RectVertex>();
	}
	
	protected BaseColorBatch<Vertex3D> createColorBatch(){
		return new BaseColorBatch<Vertex3D>();
	}
	
	public int getDrawCalls() {
		return drawCalls;
	}

	public boolean isEnablePost() {
		return enablePost;
		//return false;
	}

	public BaseCanvas translate(float tx,float ty){
		getData().translate(tx,ty);
		return this;
	}
	
	public BaseCanvas rotate(float r){
		getData().rotate(r);
		return this;
	}
	
	public BaseCanvas rotate(float ox,float oy,float r){
		getData().rotate(ox,oy,r);
		return this;
	}
	
	public BaseCanvas scale(float x,float y){
		getData().scale(x,y);
		return this;
	}

	public BaseCanvas scaleContent(float s){
		getData().scaleContent(s);
		return this;
	}

	public BaseCanvas clip(float w,float h){
		getData().clip(w,h);
		return this;
	}

	public float getPixelDensity(){
		return getData().getPixelDensity();
	}

	public float getWidth(){
		return getData().getWidth();
	}

	public float getHeight(){
		return getData().getHeight();
	}

	public void setMProjMatrix(Mat4 mProjMatrix) {
		this.getData().setCurrentProjMatrix(mProjMatrix);
	}

	public Mat4 getMProjMatrix() {
		return getData().getCurrentProjMatrix();
	}

	public Mat4 getMaskMatrix(){
		return getData().getCurrentMaskMatrix();
	}
	
	public Camera getCamera(){
		return getData().getCamera();
	}

	public float getCanvasAlpha(){
		return getData().getCanvasAlpha();
	}

	public void setCanvasAlpha(float a){
		getData().setCanvasAlpha(a);
	}
	
	/**
	 *@param p:当前应该是的状态
	 */
	public void checkPrepared(String msg,boolean p){
		if(p!=isPrepared()){
			throw new GLException("prepare err [n,c]=["+p+","+isPrepared()+"] msg: "+msg);
		}
	}
	
	public abstract boolean isPrepared();
	public abstract void prepare();
	public abstract void unprepare();

	@Override
	public void onSave(CanvasData t) {

	}

	@Override
	public void onRestore(CanvasData now,CanvasData pre) {

		pre.recycle();
	}

	public void delete(){
		recycle();
	}

	/*
	public Mat4 getFinalMatrix(){
		return getData().getFinalMatrix();
	}
	*/

	private void injectData(BaseBatch batch,AbstractTexture texture,float alpha,Color4 mixColor,Texture3DShader shader){
		shader.useThis();
		shader.loadMixColor(mixColor);
		shader.loadAlpha(alpha*getCanvasAlpha());
		shader.loadMatrix(getCamera());
		shader.loadTexture(texture.getTexture());
		shader.loadBatch(batch);
	}

	public void injectRectData(RectF drawingRect,Vec4 padding,RectTextureShader shader){
		shader.useThis();
		shader.loadRectData(drawingRect,padding);
	}

	public void injectRoundedRectData(RectF drawingRect,Vec4 padding,float radius,Color4 glowColor,float glowFactor,RoundedRectTextureShader shader){
		shader.useThis();
		shader.loadRectData(drawingRect,padding,radius,glowColor,glowFactor);
	}

	public void drawTexture3DBatch(BaseBatch batch,AbstractTexture texture,float alpha,Color4 mixColor){
		checkCanDraw();
		injectData(batch,texture.getTexture(),alpha,mixColor,getData().getTexture3DShader());
		getData().getTexture3DShader().applyToGL(GLWrapped.GL_TRIANGLES, 0, batch.getVertexCount());
	}

	public void drawTexture3DBatch(BaseBatch batch,AbstractTexture t){
		drawTexture3DBatch(batch,t,1,Color4.ONE);
	}
	
	/*
	private TextureVertex3D[] createBaseVertexs(AbstractTexture texture,IQuad res,IQuad dst,Color4 color,float z){
		//  3          2
		//   ┌────┐
		//   └────┘
		//  0          1
		final TextureVertex3D v0=new TextureVertex3D();
		v0.setPosition(dst.getBottomLeft(),z);
		v0.setColor(color);
		v0.setTexturePoint(texture.toTexturePosition(res.getBottomLeft()));
		final TextureVertex3D v1=new TextureVertex3D();
		v1.setPosition(dst.getBottomRight(),z);
		v1.setColor(color);
		v1.setTexturePoint(texture.toTexturePosition(res.getBottomRight()));
		final TextureVertex3D v2=new TextureVertex3D();
		v2.setPosition(dst.getTopRight(),z);
		v2.setColor(color);
		v2.setTexturePoint(texture.toTexturePosition(res.getTopRight()));
		final TextureVertex3D v3=new TextureVertex3D();
		v3.setPosition(dst.getTopLeft(),z);
		v3.setColor(color);
		v3.setTexturePoint(texture.toTexturePosition(res.getTopLeft()));
		return new TextureVertex3D[]{v0,v1,v2,v3};
	}
	*/
	
	public static TextureVertex3D[] createBaseVertexs(IQuad textureQuad,IQuad dst,Color4 color,float z){
		//  3          2
		//   ┌────┐
		//   └────┘
		//  0          1
		final TextureVertex3D v0=new TextureVertex3D();
		v0.setPosition(dst.getBottomLeft(),z);
		v0.setColor(color);
		v0.setTexturePoint(textureQuad.getBottomLeft());
		final TextureVertex3D v1=new TextureVertex3D();
		v1.setPosition(dst.getBottomRight(),z);
		v1.setColor(color);
		v1.setTexturePoint(textureQuad.getBottomRight());
		final TextureVertex3D v2=new TextureVertex3D();
		v2.setPosition(dst.getTopRight(),z);
		v2.setColor(color);
		v2.setTexturePoint(textureQuad.getTopRight());
		final TextureVertex3D v3=new TextureVertex3D();
		v3.setPosition(dst.getTopLeft(),z);
		v3.setColor(color);
		v3.setTexturePoint(textureQuad.getTopLeft());
		return new TextureVertex3D[]{v0,v1,v2,v3};
	}

	
	public static RectVertex[] createRectVertexs(AbstractTexture texture,IQuad res,IQuad dst,Color4 color,float z){
		//  3          2
		//   ┌────┐
		//   └────┘
		//  0          1
		final RectVertex v0=RectVertex.atRect(dst,0,1);
		v0.setPosition(new Vec3(dst.getBottomLeft(),z));
		v0.setColor(color);
		v0.setTexturePoint(texture.toTexturePosition(res.getBottomLeft()));
		final RectVertex v1=RectVertex.atRect(dst,1,1);
		v1.setPosition(new Vec3(dst.getBottomRight(),z));
		v1.setColor(color);
		v1.setTexturePoint(texture.toTexturePosition(res.getBottomRight()));
		final RectVertex v2=RectVertex.atRect(dst,1,0);
		v2.setPosition(new Vec3(dst.getTopRight(),z));
		v2.setColor(color);
		v2.setTexturePoint(texture.toTexturePosition(res.getTopRight()));
		final RectVertex v3=RectVertex.atRect(dst,0,0);
		v3.setPosition(new Vec3(dst.getTopLeft(),z));
		v3.setColor(color);
		v3.setTexturePoint(texture.toTexturePosition(res.getTopLeft()));
		return new RectVertex[]{v0,v1,v2,v3};
	}

	public TextureVertex3D[] makeupVertex(AbstractTexture texture,Vec2[] resV,Vec2[] dstV,Color4 varyColors){
		TextureVertex3D[] ary=new TextureVertex3D[resV.length];
		Vec2 curRes;
		Vec2 curDst;
		for(int i=0;i<ary.length;i++){
			curRes=resV[i];
			curDst=dstV[i];
			ary[i]=TextureVertex3D.atPosition(new Vec3(curDst.x,curDst.y,defZ))
				.setTexturePoint(texture.toTexturePosition(curRes.x,curRes.y))
				.setColor(varyColors);
		}
		return ary;
	}

	/**
	 *@param res:此处为Texture范围，使用实际像素坐标（原点左上）
	 *@param dst:绘制在canvas上的坐标，也是实际像素坐标（原点左下）
	 */
	private GLTexture preTexture;
	private BlendType preBlend;

	public void enablePost() {
		this.enablePost=true;
	}
	
	public void disablePost(){
		postDraw();
		this.enablePost=false;
	}
	
	
	public void postDraw(){
		if(!isEnablePost())return;
		/*
		 GLPaint paint=new GLPaint();
		 drawColorBatch(paint,tmpColorBatch);
		 tmpColorBatch.clear();
		 */
		if(preTexture==null&&preBlend==null)return;
		onPostTextureChange(preTexture);
		drawTexture3DBatch(tmpBatch,preTexture,1,Color4.ONE);
		tmpBatch.clear();
		preTexture=null;
		preBlend=null;
	}

	protected void checkPost(AbstractTexture texture){
		if(texture.getTexture()!=preTexture||getBlendSetting().getBlendType()!=preBlend
		   ||(tmpBatch!=null&&tmpBatch.getVertexCount()>maxBatchSize)){
			if(preTexture!=null&&preBlend!=null){
				BlendType nowBlend=getBlendSetting().getBlendType();
				boolean changeBlend=(preBlend!=nowBlend);
				if(changeBlend)getBlendSetting().setBlendType(preBlend);
				postDraw();
				if(changeBlend)getBlendSetting().setBlendType(nowBlend);
				if(changeBlend)onPostBlendingChange(nowBlend);
			}
			preTexture=texture.getTexture();
			preBlend=getBlendSetting().getBlendType();
		}
	}
	
	protected void onPostBlendingChange(BlendType type){
		
	}

	protected void onPostTextureChange(GLTexture texture){
		
	}
	
	public void drawTexture(AbstractTexture texture,IQuad res,IQuad dst,Color4 mixColor,Color4 varyColor,float z,float alpha){
		//checkCanDraw();
		drawCalls++;
		if(isEnablePost()){
			checkPost(texture);
			varyColor=varyColor.copyNew().multiple(mixColor).multiple(alpha);
			final TextureVertex3D[] v=createBaseVertexs(texture.toTextureQuad(res),dst,varyColor,z);
			tmpBatch.add(v[0],v[1],v[2],v[0],v[2],v[3]);
			//Arrays.fill(v,null);
		}else{
			final TextureVertex3D[] v=createBaseVertexs(texture.toTextureQuad(res),dst,varyColor,z);
			tmpBatch.add(v[0],v[1],v[2],v[0],v[2],v[3]);
			drawTexture3DBatch(tmpBatch,texture,alpha,mixColor);
			tmpBatch.clear();
			//Arrays.fill(v,null);
		}
	}
	
	public void drawTexture(AbstractTexture texture,IQuad dst,Color4 mixColor,Color4 varyColor,float z,float alpha){
		//checkCanDraw();
		drawCalls++;
		if(isEnablePost()){
			checkPost(texture);
			varyColor=varyColor.copyNew().multiple(mixColor).multiple(alpha);
			final TextureVertex3D[] v=createBaseVertexs(texture.getRawQuad(),dst,varyColor,z);
			tmpBatch.add(v[0],v[1],v[2],v[0],v[2],v[3]);
			//Arrays.fill(v,null);
		}else{
			final TextureVertex3D[] v=createBaseVertexs(texture.getRawQuad(),dst,varyColor,z);
			tmpBatch.add(v[0],v[1],v[2],v[0],v[2],v[3]);
			drawTexture3DBatch(tmpBatch,texture,alpha,mixColor);
			tmpBatch.clear();
			//Arrays.fill(v,null);
		}
	}


	public void drawTexture(AbstractTexture texture,Vec2[] resV,Vec2[] dstV,Color4 varyColor,float finalAlpha,Color4 mixColor){
		//checkCanDraw();
		drawCalls++;
		if(isEnablePost()){
			checkPost(texture);
			varyColor=varyColor.copyNew().multiple(mixColor).multiple(finalAlpha);
			tmpBatch.add(makeupVertex(texture,resV,dstV,varyColor));
		}else{
			tmpBatch.add(makeupVertex(texture,resV,dstV,varyColor));
			drawTexture3DBatch(tmpBatch,texture,finalAlpha,mixColor);
			tmpBatch.clear();
		}
	}

	public void drawTexture(AbstractTexture texture,IQuad res,IQuad dst,GLPaint paint){
		drawTexture(texture,res,dst,paint.getMixColor(),paint.getVaryingColor(),defZ,paint.getFinalAlpha());
	}

	public void drawTexture(AbstractTexture texture,IQuad dst,Color4 varyingColor,float alpha){
		drawTexture(texture,dst,Color4.ONE,varyingColor,defZ,alpha);
	}

	public void drawTexture(AbstractTexture texture,IQuad res,IQuad dst,Color4 mixColor,Color4 color,float alpha){
		drawTexture(texture,res,dst,mixColor,color,defZ,alpha);
	}

	public void drawRectTexture(AbstractTexture texture,IQuad res,RectF dst,GLPaint paint){
		checkCanDraw();
		tmpRectBatch.clear();
		//tmpRectBatch.setColorMixRate(paint.getColorMixRate());
		RectVertex[] v=createRectVertexs(texture,res,dst,paint.getVaryingColor(),defZ);
		tmpRectBatch.add(v[0],v[1],v[2],v[0],v[2],v[3]);
		drawRectBatch(tmpRectBatch,texture,dst,paint);
	}

	public void drawRoundedRectTexture(AbstractTexture texture,IQuad res,RectF dst,GLPaint paint){
		checkCanDraw();
		tmpRectBatch.clear();
		//tmpRectBatch.setColorMixRate(paint.getColorMixRate());
		RectVertex[] v=createRectVertexs(texture,res,dst,paint.getVaryingColor(),defZ);
		tmpRectBatch.add(v[0],v[1],v[2],v[0],v[2],v[3]);
		drawRoundedRectBatch(tmpRectBatch,texture,dst,paint);
	}

	public void drawTexture(AbstractTexture texture,IQuad dst,GLPaint paint){
		drawTexture(texture,dst,paint.getMixColor(),paint.getVaryingColor(),defZ,paint.getFinalAlpha());
	}

	public void drawTexture(AbstractTexture texture,IQuad dst,Color4 mixColor,Color4 color,float alpha){
		drawTexture(texture,dst,mixColor,color,defZ,alpha);
	}

	public void drawRectBatch(RectVertexBatch batch,AbstractTexture texture,RectF dst,GLPaint paint){
		checkCanDraw();
		RectTextureShader shader=getData().getShaders().getRectShader();
		injectData(batch,texture,paint.getFinalAlpha(),paint.getMixColor(),shader);
		injectRectData(dst,paint.getPadding(),shader);
		shader.applyToGL(GLWrapped.GL_TRIANGLES, 0, batch.getVertexCount());
	}

	public void drawRoundedRectBatch(RectVertexBatch batch,AbstractTexture texture,RectF dst,GLPaint paint){
		checkCanDraw();
		RoundedRectTextureShader shader=getData().getShaders().getRoundedRectShader();
		injectData(batch,texture,paint.getFinalAlpha(),paint.getMixColor(),shader);
		injectRoundedRectData(dst,paint.getPadding(),paint.getRoundedRadius(),paint.getGlowColor(),paint.getGlowFactor(),shader);
		shader.applyToGL(GLWrapped.GL_TRIANGLES, 0, batch.getVertexCount());
	}

	public Vertex3D[] createLineRectVertex(Vec2 start,Vec2 end,float w,Color4 color){
		Vec2 dirt=end.copy().minus(start);
		dirt.toOrthogonalDirectionNormal().zoom(w);
		Vertex3D v1=Vertex3D
			.atPosition(new Vec3(start.copy().add(dirt),defZ))
			.setColor(color);
		Vertex3D v2=Vertex3D
			.atPosition(new Vec3(start.copy().minus(dirt),defZ))
			.setColor(color);
		Vertex3D v3=Vertex3D
			.atPosition(new Vec3(end.copy().add(dirt),defZ))
			.setColor(color);
		Vertex3D v4=Vertex3D
			.atPosition(new Vec3(end.copy().minus(dirt),defZ))
			.setColor(color);
		return new Vertex3D[]{v1,v2,v3,v4};
	}



	public static Vertex3D[] rectToTriangles(Vertex3D[] v){
		return new Vertex3D[]{v[0],v[1],v[3],v[0],v[3],v[2]};
	}
	
	public static TextureVertex3D[] rectToTriangles(TextureVertex3D[] v){
		return new TextureVertex3D[]{v[0],v[1],v[3],v[0],v[3],v[2]};
	}

	public Vertex3D[] createLineRectVertexTriangles(Vec2 start,Vec2 end,float w,Color4 color){
		Vertex3D[] v=createLineRectVertex(start,end,w,color);
		return rectToTriangles(v);
	}

	public void clearTmpColorBatch(){
		tmpColorBatch.clear();
	}

	private void postToColorBatch(Vertex3D[] v){
		tmpColorBatch.add(v);
	}

	public void addToColorBatch(Vertex3D[] vs,GLPaint paint){
		/*if(enablePost){
		 for(Vertex3D v:vs){
		 v.color.multiple(paint.getMixColor()).multiple(paint.getFinalAlpha());
		 }
		 postToColorBatch(vs);
		 }else{*/
		postToColorBatch(vs);
		//}
	}

	public void drawColorBatch(GLPaint paint,BaseColorBatch cbatch){
		ColorShader shader=getData().getShaders().getColorShader();
		shader.useThis();
		shader.loadPaint(paint,getCanvasAlpha());
		shader.loadMatrix(getCamera());
		shader.loadBatch(cbatch);
		shader.applyToGL(GLWrapped.GL_TRIANGLES,0,cbatch.getVertexCount());
	}

	public void postColorBatch(GLPaint paint){
		drawColorBatch(paint,tmpColorBatch);
	}

	/*
	 public void drawRect(IQuad quad,Color4 color){

	 }
	 */

	public void drawLines(Vec2[] lines,GLPaint paint){
		tmpColorBatch.clear();
		for(int i=0;i<lines.length;i+=2){
			addToColorBatch(
				createLineRectVertexTriangles(
					lines[i],lines[i+1],
					paint.getStrokeWidth(),
					paint.getVaryingColor()
				),
				paint
			);
		}
		postColorBatch(paint);
	}

	public void drawLines(float[] lines,GLPaint paint){
		tmpColorBatch.clear();
		for(int i=0;i<lines.length;i+=4){
			addToColorBatch(
				createLineRectVertexTriangles(
					new Vec2(lines[i],lines[i+1]),
					new Vec2(lines[i+2],lines[i+3]),
					paint.getStrokeWidth(),
					paint.getVaryingColor()
				),
				paint
			);
		}
		postColorBatch(paint);
	}

	public void drawLine(float x1,float y1,float x2,float y2,GLPaint paint){
		drawLines(new float[]{x1,y1,x2,y2},paint);
	}

	public void drawLine(Vec2 v1,Vec2 v2,GLPaint paint){
		drawLine(v1.x,v1.y,v2.x,v2.y,paint);
	}

	/**
	 *通过简便的参数以canvas上某一点为中心绘制一张Texture
	 */
	public void drawTextureAnchorCenter(AbstractTexture texture,Vec2 org,Vec2 w,GLPaint paint){
		RectF dst=RectF.ltrb(org.x-w.x,org.y-w.y,org.x+w.x,org.y+w.y);
		drawTexture(texture,new RectF(0,0,texture.getWidth(),texture.getHeight()),dst,paint);
	}

	public void drawTextureAnchorCenter(AbstractTexture texture,Vec2 org,Vec4 w,GLPaint paint){
		RectF dst=RectF.ltrb(org.x-w.r,org.y-w.g,org.x+w.b,org.y+w.a);
		drawTexture(texture,new RectF(0,0,texture.getWidth(),texture.getHeight()),dst,paint);
	}

	public abstract int getDefWidth();
	public abstract int getDefHeight();
	public abstract BlendSetting getBlendSetting();
	protected abstract void checkCanDraw();
	public abstract CanvasData getDefData();
	public abstract void clearBuffer();
	public abstract void drawColor(Color4 c);

	public Mat4 createDefProjMatrix(){
		Mat4 projMatrix=new Mat4();
		projMatrix.setOrtho(0,getDefWidth(),0,getDefHeight(),-100,100);
		return projMatrix;
	}

	@Override
	public void recycle(){
		tmpBatch.clear();
	}

	@Override
	protected void finalize() throws Throwable {

		super.finalize();
	}
}
