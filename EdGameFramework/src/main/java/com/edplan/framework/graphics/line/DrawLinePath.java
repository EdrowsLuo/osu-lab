package com.edplan.framework.graphics.line;
import com.edplan.framework.graphics.opengl.drawui.DrawInfo;
import com.edplan.framework.graphics.opengl.buffer.Vec3Buffer;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.FMath;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.utils.MLog;
import com.edplan.framework.graphics.opengl.batch.BaseColorBatch;
import java.util.ArrayList;

public class DrawLinePath<T extends BaseColorBatch>
{
	public static final int MAXRES=24;
	
	public static final float Z_MIDDLE=1.0f;
	
	public static final float Z_SIDE=0.0f;
	
	private BaseColorBatch batch;
	
	private AbstractPath path;
	
	private Vec2 textureStart=new Vec2(0,0);
	
	private Vec2 textureEnd=new Vec2(1,1);
	
	private DrawInfo info;
	
	private ArrayList<TextureVertex3D[]> bufferedLineQuad=new ArrayList<TextureVertex3D[]>();
	
	private ArrayList<TextureVertex3D[]> bufferedLineCap=new ArrayList<TextureVertex3D[]>();
	
	public DrawLinePath(AbstractPath p){
		path=p;
		info=new DrawInfo(){
			@Override
			public Vec2 toLayerPosition(Vec2 v) {

				return v;
			}

			@Override
			public Color4 getVaryingColor(Vec2 position) {

				return Color4.White;
			}
		};
	}

	public void setDrawInfo(DrawInfo i){
		info=i;
	}
	
	private void addLineCap(Vec2 org,float theta,float thetaDiff){
		final float step=FMath.Pi/MAXRES;
		
		float dir=Math.signum(thetaDiff);
		thetaDiff*=dir;
		//MLog.test.vOnce("dir","gl_test","dir: "+dir);
		int amountPoints=(int)Math.ceil(thetaDiff/step);
		
		if(dir<0)
			theta+=FMath.Pi;
		
		/* current = org + atCircle(...)*width */
		Vec2 current=Vec2.atCircle(theta).zoom(path.getWidth()).add(org);
		current=info.toLayerPosition(current);
		Color4 currentColor=info.getVaryingColor(current);
		
		Vec2 orgAtLayer=info.toLayerPosition(org);
		Color4 orgColor=info.getVaryingColor(orgAtLayer);
		
		Vec3 orgAtLayer3D=new Vec3(orgAtLayer,Z_MIDDLE);
		for(int i=1;i<=amountPoints;i++){
			batch.add(
				TextureVertex3D
					 .atPosition(orgAtLayer3D)
					 .setTexturePoint(textureStart)
					 .setColor(orgColor));
			batch.add(
				TextureVertex3D
					 .atPosition(new Vec3(current,Z_SIDE))
					 .setTexturePoint(textureEnd)
					 .setColor(currentColor));
			
			float angularOffset=Math.min(i*step,thetaDiff);
			/* current = org+atCircle(...)*width*/
			current=
			info.toLayerPosition(
				Vec2.atCircle(theta+dir*angularOffset).zoom(path.getWidth()).add(org));
			currentColor=info.getVaryingColor(current);
			
			batch.add(
				TextureVertex3D
					 .atPosition(new Vec3(current,Z_SIDE))
					 .setTexturePoint(textureEnd)
					 .setColor(currentColor));
		}
	}
	
	private void addLineQuads(Vec2 ps,Vec2 pe){
		Vec2 oth_expand=Vec2.lineOthNormal(ps,pe).zoom(path.getWidth());
		
		Vec2 startL=info.toLayerPosition(ps.copy().add(oth_expand));
		Vec2 startR=info.toLayerPosition(ps.copy().minus(oth_expand));
		Vec2 endL=info.toLayerPosition(pe.copy().add(oth_expand));
		Vec2 endR=info.toLayerPosition(pe.copy().minus(oth_expand));
		Vec2 start=info.toLayerPosition(ps);
		Vec2 end=info.toLayerPosition(pe);
		
		Vec3 start3D=new Vec3(start,Z_MIDDLE);
		Vec3 end3D=new Vec3(end,Z_MIDDLE);
		Color4 startColor=info.getVaryingColor(start);
		Color4 endColor=info.getVaryingColor(end);
		
		//tr 1
		batch.add(
			TextureVertex3D
			 .atPosition(start3D)
			 .setTexturePoint(textureStart)
			 .setColor(startColor));

		batch.add(
			TextureVertex3D
			 .atPosition(end3D)
			 .setTexturePoint(textureStart)
			 .setColor(endColor));

		batch.add(
			TextureVertex3D
			 .atPosition(new Vec3(endL,Z_SIDE))
			 .setTexturePoint(textureEnd)
			 .setColor(info.getVaryingColor(endL)));

		//tr 2
		batch.add(
			TextureVertex3D
			 .atPosition(start3D)
			 .setTexturePoint(textureStart)
			 .setColor(startColor));

		batch.add(
			TextureVertex3D
			 .atPosition(new Vec3(endL,Z_SIDE))
			 .setTexturePoint(textureEnd)
			 .setColor(info.getVaryingColor(endL)));

		batch.add(
			TextureVertex3D
			 .atPosition(new Vec3(startL,Z_SIDE))
			 .setTexturePoint(textureEnd)
			 .setColor(info.getVaryingColor(startL)));

		//tr 3
		batch.add(
			TextureVertex3D
			 .atPosition(start3D)
			 .setTexturePoint(textureStart)
			 .setColor(startColor));

		batch.add(
			TextureVertex3D
			 .atPosition(end3D)
			 .setTexturePoint(textureStart)
			 .setColor(endColor));

		batch.add(
			TextureVertex3D
			 .atPosition(new Vec3(endR,Z_SIDE))
			 .setTexturePoint(textureEnd)
			 .setColor(info.getVaryingColor(endR)));

		//tr 4
		batch.add(
			TextureVertex3D
			 .atPosition(start3D)
			 .setTexturePoint(textureStart)
			 .setColor(startColor));

		batch.add(
			TextureVertex3D
			 .atPosition(new Vec3(endR,Z_SIDE))
			 .setTexturePoint(textureEnd)
			 .setColor(info.getVaryingColor(endR)));

		batch.add(
			TextureVertex3D
			 .atPosition(new Vec3(startR,Z_SIDE))
			 .setTexturePoint(textureEnd)
			 .setColor(info.getVaryingColor(startR)));
	}
	
	public void addToBatch(BaseColorBatch<?> batch){
		this.batch=batch;
		if(path.size()<2){
			if(path.size()==1){
				addLineCap(path.get(0),FMath.Pi,FMath.Pi);
				addLineCap(path.get(0),0,FMath.Pi);
				return;
			}else{
				return;
				//throw new RuntimeException("Path must has at least 1 point");
			}
		}
		
		float theta=Vec2.calTheta(path.get(0),path.get(1));
		addLineCap(path.get(0),theta+FMath.PiHalf,FMath.Pi);
		addLineQuads(path.get(0),path.get(1));
		if(path.size()==2){
			addLineCap(path.get(1),theta-FMath.PiHalf,FMath.Pi);
			return;
		}
		Vec2 nowPoint=path.get(1);
		Vec2 nextPoint;
		float preTheta=theta;
		float nextTheta;
		int max_i=path.size();
		for(int i=2;i<max_i;i++){
			nextPoint=path.get(i);
			nextTheta=Vec2.calTheta(nowPoint,nextPoint);
			addLineCap(nowPoint,preTheta-FMath.PiHalf,nextTheta-preTheta);
			addLineQuads(nowPoint,nextPoint);
			nowPoint=nextPoint;
			preTheta=nextTheta;
		}
		addLineCap(path.get(max_i-1),preTheta-FMath.PiHalf,FMath.Pi);
	}
}
