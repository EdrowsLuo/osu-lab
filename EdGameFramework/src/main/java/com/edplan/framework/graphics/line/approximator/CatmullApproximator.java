package com.edplan.framework.graphics.line.approximator;

import com.edplan.framework.math.Vec2;
import java.util.ArrayList;
import java.util.List;

public class CatmullApproximator 
{
	private final int detail=50;

	private List<Vec2> controlPoints;

	public CatmullApproximator(List<Vec2> controlPoints){
		this.controlPoints=controlPoints;
	}
	
	public List<Vec2> createCatmull(){
		ArrayList<Vec2> result=new ArrayList<Vec2>();

		for (int i=0;i<controlPoints.size()-1;i++){
			Vec2 v1=i>0?controlPoints.get(i-1):controlPoints.get(i);
			Vec2 v2=controlPoints.get(i);
			Vec2 v3=i<controlPoints.size()-1?controlPoints.get(i+1):v2.copy().add(v2).minus(v1);
			Vec2 v4=i<controlPoints.size()-2?controlPoints.get(i+2):v3.copy().add(v3).minus(v2);

			for (int c=0; c<detail; c++){
				result.add(findPoint(v1,v2,v3,v4,(float)c/detail));
				result.add(findPoint(v1,v2,v3,v4,(float)(c+1)/detail));
			}
		}

		return result;
	}
	
	private Vec2 findPoint(Vec2 vec1,Vec2 vec2,Vec2 vec3,Vec2 vec4,float t) {
		float t2= t*t;
		float t3=t*t2;
		Vec2 result=new Vec2();
		result.x=0.5f*(2f*vec2.x+(-vec1.x+vec3.x)*t+(2f*vec1.x-5f*vec2.x+4f*vec3.x-vec4.x)*t2+(-vec1.x+3f*vec2.x-3f*vec3.x+vec4.x)*t3);
		result.y=0.5f*(2f*vec2.y+(-vec1.y+vec3.y)*t+(2f*vec1.y-5f*vec2.y+4f*vec3.y-vec4.y)*t2+(-vec1.y+3f*vec2.y-3f*vec3.y+vec4.y)*t3);
		return result;
	}
}
