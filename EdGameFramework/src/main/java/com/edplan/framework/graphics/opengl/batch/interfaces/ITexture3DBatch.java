package com.edplan.framework.graphics.opengl.batch.interfaces;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.base.IHasColor;
import com.edplan.framework.graphics.opengl.batch.base.IHasPosition;
import com.edplan.framework.graphics.opengl.batch.base.IHasTexturePosition;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.interfaces.Addable;

public interface ITexture3DBatch<T extends TextureVertex3D> extends BaseBatch<T>,IHasPosition,IHasTexturePosition,IHasColor
{
	
}
