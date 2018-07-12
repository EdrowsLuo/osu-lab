package com.edplan.framework.graphics.opengl.bufferObjects;
import java.util.ArrayList;
import java.util.Iterator;
import android.util.Log;
import com.edplan.framework.graphics.layer.BufferedLayer;

public class FBOPool
{
	private int maxMemory=1080*1920*10*0;
	
	private int currentMemory=0;
	
	private int minWidth=500;
	
	private int minHeight=500;
	
	private ArrayList<WrappedEntry> list=new ArrayList<WrappedEntry>();
	
	
	/**
	 *想内存池申请一个width,height的FBO，返回的FBO长宽不小于申请的，当没申请到的时候返回null
	 */
	public FrameBufferObject requireFBO(int width,int height){
		Iterator<WrappedEntry> iter=list.iterator();
		WrappedEntry e;
		while(iter.hasNext()){
			e=iter.next();
			if(e.width>=width&&e.height>=height){
				iter.remove();
				FrameBufferObject fbo=e.fbo;
				fbo.setBound(width,height);
				//if(fbo.isBind())fbo.unBind();
				currentMemory-=e.width*e.height;
				//Log.v("fbo-pool","pop a "+e.width+"x"+e.height+" fbo. Current memory:"+currentMemory+" cur size: "+list.size());
				return fbo;
			}
		}
		return null;
	}
	
	
	/**
	 *返回fbo是否被保存
	 */
	public boolean saveFBO(FrameBufferObject fbo){
		//Log.v("fbo-pool","onSaveFbo "+fbo.getFBOId());
		if(fbo==null)return false;
		if(fbo instanceof FrameBufferObject.SystemFrameBuffer){
			throw new RuntimeException("you can't save SystemBuffer to pool");
		}
		
		
		if(fbo.isBind())throw new RuntimeException("you can't save a binded fbo into pool");
		if(fbo.getCreatedHeight()<minHeight||fbo.getCreatedWidth()<minWidth){
			//不保存过小的fbo
			return false;
		}else{
			if(currentMemory>maxMemory){
				return false;
			}else{
				list.add(new WrappedEntry(fbo));
				currentMemory+=fbo.getCreatedHeight()*fbo.getCreatedWidth();
				return true;
			}
		}
	}
	
	public static void initialGL(){
		/*for(int i=0;i<10;i++){
			BufferedLayer.DEF_FBOPOOL.saveFBO(FrameBufferObject.create(1000,1000,true));
		}*/
	}
	
	public static class WrappedEntry{
		
		public int width;
		public int height;
		public FrameBufferObject fbo;
		
		public WrappedEntry(FrameBufferObject fbo){
			this.fbo=fbo;
			this.width=fbo.getCreatedWidth();
			this.height=fbo.getCreatedHeight();
		}
	}
	
	public static class HeightList extends ArrayList<WrappedEntry>{
		
	}
}
