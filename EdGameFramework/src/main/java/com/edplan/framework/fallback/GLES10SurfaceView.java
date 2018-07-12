package com.edplan.framework.fallback;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.test.TestStaticData;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class GLES10SurfaceView extends GLSurfaceView 
{
	
	Renderer mRenderer;
	

	public GLES10SurfaceView(Context con){
		super(con);
		this.setEGLContextClientVersion(1);
		this.setEGLConfigChooser(new MSAAConfig());
		mRenderer=new GLES10MainRenderer(getContext(),1);
		this.setRenderer(mRenderer);
		if(mRenderer instanceof OnTouchListener)this.setOnTouchListener((OnTouchListener)mRenderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	public class MSAAConfig implements EGLConfigChooser {
		@Override
		public EGLConfig chooseConfig(EGL10 egl,EGLDisplay display) {

			int attribs[] = {
				EGL10.EGL_LEVEL, 0,
				EGL10.EGL_RENDERABLE_TYPE, 4,  // EGL_OPENGL_ES2_BIT
				EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
				EGL10.EGL_RED_SIZE, 8,
				EGL10.EGL_GREEN_SIZE, 8,
				EGL10.EGL_BLUE_SIZE, 8,
				EGL10.EGL_DEPTH_SIZE, 16,
				EGL10.EGL_SAMPLE_BUFFERS, 1,
				EGL10.EGL_SAMPLES, 4,  // This is for 4x MSAA.
				EGL10.EGL_NONE
			};
			EGLConfig[] configs = new EGLConfig[1];
			int[] configCounts = new int[1];
			egl.eglChooseConfig(display, attribs, configs, 1, configCounts);

			if (configCounts[0] == 0) {
				// Failed! Error handling.
				throw new RuntimeException("err choose comfig");
				//return null;
			} else {
				return configs[0];
			}
		}


	}
}
