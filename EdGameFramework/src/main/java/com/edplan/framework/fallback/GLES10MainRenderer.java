package com.edplan.framework.fallback;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.ui.looper.UILooper;
import com.edplan.framework.utils.MLog;
import com.edplan.superutils.MTimer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;

import com.edplan.framework.graphics.opengl.GL10Canvas2D;

public class GLES10MainRenderer implements GLSurfaceView.Renderer {
    private MContext context;

    private UILooper uiLooper;

    private int glVersion;

    private int width, height;

    public GLES10MainRenderer(Context con, int glVersion) {
        context = new MContext(con);
        this.glVersion = glVersion;
    }

    private int initialCount = 0;

    @Override
    public void onSurfaceCreated(GL10 p1, EGLConfig p2) {

        try {
            GLWrapped.initial(glVersion);
            context.initial();
            GLWrapped.depthTest.set(false);
            GLWrapped.blend.setEnable(true);
            uiLooper = new UILooper();
            context.setUiLooper(uiLooper);
            tmer.initial();

            //context.setContent(new GL10TestView(context));

            initialCount++;
            Log.v("gl_initial", "initial id: " + initialCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSurfaceChanged(GL10 p1, int width, int heigth) {

        GLES10.glViewport(0, 0, width, heigth);
        this.width = width;
        this.height = heigth;
        context.setDisplaySize(width, heigth);
        GLWrapped.blend.setEnable(true);
        GLWrapped.depthTest.set(false);
        //context.getContent().onCreate();
        Log.v("ini-log", "ini-scr: " + width + ":" + heigth);
    }

    float a = 0;
    long lt = 0;
    boolean debugUi = true;
    MTimer tmer = new MTimer();

    @Override
    public void onDrawFrame(GL10 p1) {

        MLog.test.vOnce("thread", "thread", "draw-thread: " + Thread.currentThread());
        tmer.refresh();
        uiLooper.loop(tmer.getDeltaTime());

        GL10Canvas2D canvas = new GL10Canvas2D(width, height);
        canvas.getMProjMatrix().setOrtho(0, canvas.getWidth(), canvas.getHeight(), 0, -100, 100);
        if (debugUi) {
			/*if(context.getContent()!=null){
				canvas.enablePost();
				canvas.drawColor(Color4.gray(0.3f));
				((GLES10Drawable)context.getContent()).drawGL10(canvas);
				canvas.disablePost();
			}*/
        }
    }

}
