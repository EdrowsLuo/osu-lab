package com.edplan.framework.ui.drawable;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import java.lang.ref.WeakReference;

/**
 *默认绘制在整个canvas上，
 *默认不进行layout
 */
public abstract class EdDrawable
{
//	private Mat4 translationMatrix=Mat4.createIdentity();

	private WeakReference<MContext> context;

	public EdDrawable(MContext context){
		this.context=new WeakReference<MContext>(context);
	}

	public MContext getContext() {
		return context.get();
	}

	public abstract void draw(BaseCanvas canvas);
	
	public float getMinWidth(){
		return 0;
	}
	
	public float getMinHeight(){
		return 0;
	}
}
