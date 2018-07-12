package com.edplan.framework.media.video.tbv.decode;
import java.io.File;
import com.edplan.framework.resource.AResource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.edplan.framework.resource.DirResource;
import com.edplan.framework.MContext;
import com.edplan.framework.interfaces.Reflection;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.media.video.tbv.TBVException;
import java.io.IOException;
import org.json.JSONException;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public class TBVPlayer
{
	PreciseTimeline timeline;
	
	TBVRenderer renderer;
	
	AResource dir;
	
	public TBVPlayer(File file) throws FileNotFoundException{
		renderer=new TBVRenderer(new FileInputStream(file));
		dir=new DirResource(file.getParentFile());
	}
	
	public void initial(MContext context,PreciseTimeline timeline){
		this.timeline=timeline;
		try {
			renderer.initial(context, new Reflection<String,GLTexture>(){
					@Override
					public GLTexture invoke(String t) {

						try {
							return dir.loadTexture(t);
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
					}
				});
		} catch (TBVException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public float getPlayTime(){
		return renderer.getCurrentPlayTime();
	}
	
	public void update(){
		renderer.postFrame((float)timeline.frameTime());
	}
	
	
	public AbstractTexture getFrame(){
		return renderer.getResult();
	}
}
