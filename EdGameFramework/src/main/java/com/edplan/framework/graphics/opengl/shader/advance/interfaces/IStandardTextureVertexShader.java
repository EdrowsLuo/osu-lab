package com.edplan.framework.graphics.opengl.shader.advance.interfaces;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec3;

/**
 *在由MVP矩阵决定的平面上绘制一个Texture
 */
public interface IStandardTextureVertexShader
{
	public void loadTextureRect(RectF textureCoord,RectF position);
}
