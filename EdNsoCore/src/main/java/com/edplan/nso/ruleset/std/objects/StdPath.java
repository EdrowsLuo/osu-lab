package com.edplan.nso.ruleset.std.objects;
import com.edplan.superutils.math.Vct2;
import java.util.List;
import java.util.ArrayList;
import com.edplan.framework.math.Vec2;

public class StdPath
{
	private Type type;
	private List<Vec2> controlPoints;
	
	public StdPath(){
		controlPoints=new ArrayList<Vec2>();
	}
	
	public void addControlPoint(Vec2 p){
		controlPoints.add(p);
	}
	
	public void addControlPoint(float x,float y){
		addControlPoint(new Vec2(x,y));
	}

	public void setType(Type type){
		this.type=type;
	}

	public Type getType(){
		return type;
	}

	public void setControlPoints(List<Vec2> controlPoints){
		this.controlPoints=controlPoints;
	}

	public List<Vec2> getControlPoints(){
		return controlPoints;
	}
	
	public enum Type{
		Linear("L"),Perfect("P"),Bezier("B"),Catmull("C");
		
		public final String tag;
		
		Type(String t){
			tag=t;
		}
		
		public String getTag(){
			return tag;
		}
		
		public static Type forName(String n){
			switch(n){
				case "L":
					return Linear;
				case "P":
					return Perfect;
				case "B":
					return Bezier;
				case "C":
					return Catmull;
				default:return null;
			}
		}
	}
}




