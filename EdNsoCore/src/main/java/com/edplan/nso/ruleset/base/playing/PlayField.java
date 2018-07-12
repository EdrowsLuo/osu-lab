package com.edplan.nso.ruleset.base.playing;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;

/**
 *主要的游戏绘制，不包括各种ui，只绘制HitObject以及附加物件
 *基础大小为640x480，绘制时有(x,y)=(64,48)的padding
 *因此实际绘制时的canvas大小为512x384
 *
 *PlayField仅负责绘制，所有交互均在PlayingBeatmap中
 */
public abstract class PlayField<T extends PlayingBeatmap> extends EdDrawable
{
	public static final float BASE_X=640;
	public static final float BASE_Y=480;
	public static final float PADDING_X=64;
	public static final float PADDING_Y=48;
	public static final float CANVAS_SIZE_X;//512
	public static final float CANVAS_SIZE_Y;//384
	
	static{
		CANVAS_SIZE_X=BASE_X-2*PADDING_X;
		CANVAS_SIZE_Y=BASE_Y-2*PADDING_Y;
	}

	public PlayField(MContext c){
		super(c);
	}
	
	public abstract void applyBeatmap(T beatmap);
	
	/**
	 *此处绘制默认canvas为512,384
	 */
	public abstract void draw(BaseCanvas canvas);
}
