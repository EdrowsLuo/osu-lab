package com.edplan.framework.math;

public class Triangle
{
	public static Vec2 mapPoint(Vec2 o,Vec2 ox,Vec2 oy,Vec2 point){
		Vec2 vx=ox.copy().minus(o).zoom(point.x);
		Vec2 vy=oy.copy().minus(o).zoom(point.y);
		return o.copy().add(vx).add(vy);
	}
}
