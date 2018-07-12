package com.edplan.framework.graphics.line.approximator;
import com.edplan.framework.math.Vec2;
import java.util.List;
import com.edplan.framework.math.FMath;
import java.util.ArrayList;

public class CircleApproximator
{
	private Vec2 p1;
	
	private Vec2 p2;
	
	private Vec2 p3;
	
	private int amountPoints;
	
	private final float tolerance=0.1f;
	
	public CircleApproximator(Vec2 p1,Vec2 p2,Vec2 p3){
		this.p1=p1;
		this.p2=p2;
		this.p3=p3;
	}
	
	public List<Vec2> createArc(){
		if(Vec2.shareLine(p1,p2,p3,tolerance)){
			List<Vec2> l=new ArrayList<Vec2>();
			l.add(p1);
			l.add(p2);
			l.add(p3);
			return l;
		}
		float aSp=p2.copy().minus(p3).lengthSquared();
		float bSp=p1.copy().minus(p3).lengthSquared();
		float cSp=p1.copy().minus(p2).lengthSquared();
		
		float s=aSp*(bSp+cSp-aSp);
		float t=bSp*(aSp+cSp-bSp);
		float u=cSp*(aSp+bSp-cSp);
		
		float sum=s+t+u;
		
		Vec2 center=p1.copy().zoom(s)
				 .add(p2.copy().zoom(t))
				 .add(p3.copy().zoom(u))
				 .divide(sum);
				 
		Vec2 dA=p1.copy().minus(center);
		Vec2 dC=p3.copy().minus(center);
		
		float r=dA.length();
		
		float thetaStart=FMath.atan2(dA.y,dA.x);
		float thetaEnd=FMath.atan2(dC.y,dC.x);
		
		while(thetaEnd<thetaStart)thetaEnd+=FMath.Pi2;
		
		float dir=1;
		float thetaRange=thetaEnd-thetaStart;
		
		Vec2 orthoAtoC=p3.copy().minus(p1);
		orthoAtoC=new Vec2(orthoAtoC.y,-orthoAtoC.x);
		
		if(orthoAtoC.dot(p2.copy().minus(p1))<0){
			dir=-dir;
			thetaRange=FMath.Pi2-thetaRange;
		}
		
		amountPoints=(2*r<=tolerance)?2:Math.max(2,(int)Math.ceil(thetaRange/(2*Math.acos(1-tolerance/r))));
		
		List<Vec2> output=new ArrayList<Vec2>(amountPoints);
		float currTheta;
		for(int i=0;i<amountPoints;i++){
			currTheta=thetaStart+dir*i/(amountPoints-1)*thetaRange;
			output.add((new Vec2(FMath.cos(currTheta),FMath.sin(currTheta))).zoom(r).add(center));
		}
		return output;
	}
}
