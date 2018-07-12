package com.edplan.framework.math;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.RectF;

public interface Area2D
{
	public boolean inArea(Vec2 v);

	public float maxX();
	public float maxY();
	public float minX();
	public float minY();
}
