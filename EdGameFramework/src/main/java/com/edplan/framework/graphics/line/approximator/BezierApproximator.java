package com.edplan.framework.graphics.line.approximator;
import java.util.List;
import com.edplan.framework.math.Vec2;
import com.edplan.superutils.useful.ArrayUtils;
import java.util.ArrayList;
import java.util.Stack;

public class BezierApproximator
{
	private int count;
	private List<Vec2> controlPoints;
	private Vec2[] subdivisionBuffer1;
	private Vec2[] subdivisionBuffer2;
	
	private static final float tolerance=0.25f;
	private static final float tolerance_sq=0.25f*0.25f;
	
	public BezierApproximator(List<Vec2> controlPoints){
		this.controlPoints=controlPoints;
		count=controlPoints.size();
		subdivisionBuffer1=new Vec2[count];
		subdivisionBuffer2=new Vec2[count*2-1];
	}
	
	private static boolean isFlatEnough(Vec2[] controlPoints){
		for(int i=1;i<controlPoints.length-1;i++){
			if(controlPoints[i-1].copy()
					 .minus(controlPoints[i])
					 .minus(controlPoints[i])
					 .add(controlPoints[i+1])
					 .lengthSquared()>tolerance_sq*4){
						 return false;
					 }
		}
		return true;
	}
	
	private void subdivide(Vec2[] cp,Vec2[] l,Vec2[] r){
		Vec2[] mid=subdivisionBuffer1;
		ArrayUtils.copy(mid,cp,count);
		for(int i=0;i<count;i++){
			l[i]=mid[0];
			r[count-i-1]=mid[count-i-1];
			for(int j=0;j<count-i-1;j++){
				mid[j]=mid[j].copy().add(mid[j+1]).zoom(0.5f);
			}
		}
	}
	
	private void approximate(Vec2[] cp,List<Vec2> out){
		Vec2[] l=subdivisionBuffer2;
		Vec2[] r=subdivisionBuffer1;
		
		subdivide(cp,l,r);
		
		ArrayUtils.copy(l,count,r,1,count-1);
		out.add(cp[0]);
		int idx;
		for(int i=1;i<count-1;i++){
			idx=2*i;
			//Add l0+2*l1+l2
			out.add(l[idx].copy().zoom(2).add(l[idx-1]).add(l[idx+1]).zoom(0.25f));
		}
	}
	
	public List<Vec2> createBezier(){
		ArrayList<Vec2> out=new ArrayList<Vec2>();
		
		if(count==0)return out;
		
		if(count==2){
			out.add(controlPoints.get(0));
			out.add(controlPoints.get(1));
			return out;
		}
		
		Stack<Vec2[]> toFlatten=new Stack<Vec2[]>();
		Stack<Vec2[]> freeBuffers=new Stack<Vec2[]>();
		
		toFlatten.push(controlPoints.toArray(new Vec2[controlPoints.size()]));
		
		Vec2[] lc=subdivisionBuffer2;
		Vec2[] parent;
		Vec2[] rc;
		while(toFlatten.size()>0){
			parent=toFlatten.pop();
			if(isFlatEnough(parent)){
				approximate(parent,out);
				freeBuffers.push(parent);
			}else{
				rc=freeBuffers.size()>0?freeBuffers.pop():(new Vec2[count]);
				subdivide(parent,lc,rc);
				ArrayUtils.copy(parent,lc,count);
				toFlatten.push(rc);
				toFlatten.push(parent);
			}
		}
		out.add(controlPoints.get(count-1));
		return out;
	}
}
