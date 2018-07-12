package com.edplan.framework.graphics.line;
import java.util.List;
import java.util.ArrayList;
import com.edplan.framework.math.Vec2;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.edplan.framework.math.FMath;
import com.edplan.framework.utils.MLog;

public class PathMeasurer
{
	private LinePath path;
	
	private List<Float> lengthes;
	
	private List<Vec2> directions;
	
	private Vec2 endDirection;
	
	private Vec2 endPoint;
	
	private float endHalf;
	
	public PathMeasurer(LinePath path){
		this.path=path;
	}
	
	public void onAddPoint(Vec2 v,Vec2 pre){
		lengthes.add(lengthes.get(lengthes.size()-1)+Vec2.length(v,pre));
		endDirection.add(v.copy().minus(pre).toNormal());
		measureEndNormal();
	}
	
	public void clear(){
		lengthes.clear();
		path=null;
	}
	
	private void measureLengthes(){
		if(lengthes==null){
			lengthes=new ArrayList<Float>(path.size());
		}else{
			lengthes.clear();
		}
		float l=0;
		List<Vec2> list=path.getAll();
		lengthes.add(l);
		Vec2 pre=list.get(0);
		Vec2 cur;
		for(int i=1;i<list.size();i++){
			cur=list.get(i);
			l+=Vec2.length(pre,cur);
			lengthes.add(l);
			pre=cur;
		}
		//MLog.test.vOnce("let-all","path-test",Arrays.toString(lengthes.toArray(new Float[lengthes.size()])));
	}
	
	private void measureDirections(){
		directions=new ArrayList<Vec2>(path.size());
		if(path.size()<2){
			directions.add(new Vec2(1,0));
		}else{
			Vec2 cur;
			Vec2 pre=path.get(0);
			int m=path.size()-1;
			for(int i=0;i<m;i++){
				cur=path.get(i+1);
				directions.add(cur.copy().minus(pre).toNormal());
				pre=cur;
			}
			directions.add(directions.get(path.size()-2).copy());
		}
	}
	
	private void measureEndNormal(){
		endPoint=path.getLast();
		if(path.size()>=2){
			endDirection=path.getLast().copy().minus(path.get(path.size()-2)).toNormal();
			endHalf=(lengthes.get(lengthes.size()-1)+lengthes.get(lengthes.size()-2))/2;
		}else{
			endDirection=new Vec2(0,0);
		}
	}
	
	public void measure(){
		measureLengthes();
		measureDirections();
		measureEndNormal();
	}
	
	public float maxLength(){
		return lengthes.get(lengthes.size()-1);
	}
	
	/**
	 *
	 */
	public Vec2 atLength(float l){
		if(l>=maxLength()){
			return endPoint.copy().add(endDirection.copy().zoom(l-maxLength()));
		}else{
			int s=binarySearch(l);
			float ls=lengthes.get(s);
			Vec2 v=Vec2.onLineLength(path.get(s),path.get(s+1),l-ls);
			MLog.test.vOnce("vec","path-test","vec:"+v+" v1:"+path.get(s)+" v2:"+path.get(s+1)+" s:"+s);
			return v;
		}
	}
	
	public Vec2 getTangentLine(float length){
		return directions.get(binarySearch(length));
	}
	
	//l>=0
	public int binarySearch(float l){
		if(l>=maxLength()){
			return lengthes.size()-1;
		}else{
			return binarySearch(l,0,lengthes.size()-1);
		}
	}
	
	//l>=0&&l<maxLength
	public int binarySearch(float l,int start,int end){
		if(end-start<=1){
			return start;
		}else{
			int m=(start+end)/2;
			if(lengthes.get(m)<=l){
				return binarySearch(l,m,end);
			}else{
				return binarySearch(l,start,m);
			}
		}
	}
}
