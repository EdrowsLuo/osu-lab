package com.edplan.framework.graphics.opengl;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.edplan.framework.Framework;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.layer.DefBufferedLayer;
import com.edplan.framework.graphics.opengl.bufferObjects.FrameBufferObject;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.main.MainApplication;
import com.edplan.framework.test.TestStaticData;
import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.test.performance.ui.FrameRenderMonitor;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewRoot;
import com.edplan.framework.ui.looper.UILooper;
import com.edplan.framework.utils.MLog;
import com.edplan.superutils.MTimer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class MainRenderer implements GLSurfaceView.Renderer,OnTouchListener
{
	private MContext context;
	
	private MainApplication app;
	
	private DefBufferedLayer rootLayer;

	private UILooper uiLooper;
	
	private ViewRoot viewRoot;
	
	private int glVersion;
	
	/**
	 *如果使用自定义的Renderer并通过initialWithRenderer模式初始化，
	 *请保证有一个和下面的一样的参数的构造器
	 */
	public MainRenderer(MContext context,MainApplication app){
		this.context=context;
		this.app=app;
		viewRoot=new ViewRoot(context);
		context.setViewRoot(viewRoot);
	}
	
	protected void onCreate(){
		
	}
	
	public ViewRoot getViewRoot(){
		return viewRoot;
	}

	public void setGlVersion(int glVersion) {
		this.glVersion=glVersion;
	}

	public int getGlVersion() {
		return glVersion;
	}
	
	public abstract EdView createContentView(MContext c);

	public DefBufferedLayer getRootLayer() {
		return rootLayer;
	}

	public void register(BaseGLSurfaceView view){
		context.setHoldingView(view);
	}
	
	@Override
	public boolean onTouch(View p1,MotionEvent e) {

		//Log.v("thread","touch-thread: "+Thread.currentThread());
		TestStaticData.touchPosition.set(e.getX(),e.getY());
		if(hasCreate)viewRoot.postNativeEvent(e);
		return true;
	}

	private int initialCount=0;
	@Override
	public void onSurfaceCreated(GL10 p1,EGLConfig p2) {

		try {
			GLWrapped.initial(glVersion);
			context.initial();
			GLWrapped.depthTest.set(false);
			GLWrapped.blend.setEnable(true);
			uiLooper=new UILooper();
			context.setUiLooper(uiLooper);
			tmer.initial();
			initialCount++;
			onCreate();
			Log.v("gl_initial","initial id: "+initialCount);
			//context.toast("SurfaceCreate");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean hasCreate=false;
	@Override
	public void onSurfaceChanged(GL10 p1,int width,int heigth) {

		//context.toast("SurfaceChange ["+width+","+heigth+"] "+id);
		//System.out.println("SurfaceChange ["+width+","+heigth+"] "+id);
		context.setDisplaySize(width,heigth);
		viewRoot.onChange(width,heigth);
		
		rootLayer=new DefBufferedLayer(context,width,heigth);
		rootLayer.checkChange();
		rootLayer.prepare();
		
		if(!hasCreate){
			hasCreate=true;
			app.onGLCreate();
			for(int i=0;i<10;i++){
				BufferedLayer.DEF_FBOPOOL.saveFBO(FrameBufferObject.create(1000,1000,true));
			}
			viewRoot.setContentView(createContentView(context));
			viewRoot.onCreate();
		}
		
		//Log.v("ini-log","ini-scr: "+width+":"+heigth);
	}

	float a=0;
	long lt=0;
	boolean debugUi=true;
	MTimer tmer=new MTimer();
	@Override
	public final void onDrawFrame(GL10 p1) {

		MLog.test.vOnce("thread","thread","draw-thread: "+Thread.currentThread());
		tmer.refresh();
		Tracker.reset();
		Tracker.TotalFrameTime.watch();
		GLWrapped.onFrame();
		uiLooper.loop(tmer.getDeltaTime());
		GLCanvas2D canvas=new GLCanvas2D(rootLayer);
		canvas.prepare();
		canvas.getMProjMatrix().setOrtho(0,canvas.getWidth(),canvas.getHeight(),0,-100,100);
		context.getUiLooper().handlerExpensiveTask();
		canvas.drawColor(Color4.gray(0.0f));
		viewRoot.onNewFrame(canvas,tmer.getDeltaTime());
		canvas.unprepare();
		Tracker.TotalFrameTime.end();
		/*
		if(Tracker.TotalFrameTime.totalTimeMS<14.5){
			try
			{
				double time=14.5-Tracker.TotalFrameTime.totalTimeMS;
				Thread.currentThread().sleep((int)time,Framework.msToNm(time%1));
			}
			catch (InterruptedException e)
			{}
		}
		*/
		FrameRenderMonitor.update(context);
	}
	
	
	public class LogClock{
		double keyTime;
		int idx;
		
		public void start(){
			idx=0;
			keyTime=Framework.relativePreciseTimeMillion();
		}
		
		public void log(){
			System.out.println(idx++ +":"+(Framework.relativePreciseTimeMillion()-keyTime));
			keyTime=Framework.relativePreciseTimeMillion();
		}
	}
}
