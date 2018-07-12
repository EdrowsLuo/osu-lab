package com.edplan.nso.storyboard;
import com.edplan.framework.media.video.tbv.decode.TBVPlayer;
import com.edplan.framework.MContext;
import com.edplan.framework.timing.PreciseTimeline;
import java.io.File;
import java.io.FileNotFoundException;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class TBVStoryboard
{
	private TBVPlayer player;
	
	
	public TBVStoryboard(MContext c,File f,PreciseTimeline timeline){
		try {
			player=new TBVPlayer(f);
			player.initial(c,timeline);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(BaseCanvas canvas){
		player.update();
		AbstractTexture t=player.getFrame();
		if(t!=null){
			canvas.drawTexture(t,RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight()),Color4.ONE,1);
		}
	}
	
}
