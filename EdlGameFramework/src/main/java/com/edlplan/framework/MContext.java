package com.edlplan.framework;

import android.content.Context;
import android.widget.Toast;

import com.edlplan.framework.graphics.opengl.BaseGLSurfaceView;
import com.edlplan.framework.graphics.opengl.shader.compile.CompileRawStringStore;
import com.edlplan.framework.resource.advance.ApplicationAssetResource;
import com.edlplan.framework.ui.ViewRoot;
import com.edlplan.framework.ui.looper.UILooper;
import com.edlplan.framework.ui.looper.UIStep;
import com.edlplan.framework.timing.MTimer;
import com.edlplan.framework.timing.IRunnableHandler;

import java.util.HashMap;

public class MContext {
    private Thread mainThread;

    private MTimer looperTimer;

    private Context androidContext;

    private BaseGLSurfaceView holdingView;

    private ApplicationAssetResource assetResource;

    private UILooper uiLooper;

    private int displayWidth, displayHeight;

    private ViewRoot viewRoot;

    private HashMap<String, Object> datatable = new HashMap<String, Object>();

    public MContext(Context androidContext) {
        this.androidContext = androidContext;
    }

    public void putData(String key, Object v) {
        datatable.put(key, v);
    }

    public Object getData(String key) {
        return datatable.get(key);
    }

    public void toast(final String msg) {
        if (holdingView != null) {
            holdingView.post(() -> Toast.makeText(androidContext, msg, Toast.LENGTH_SHORT).show());
        }
    }

    public void postToNativeView(Runnable runnable) {
        if (holdingView != null) {
            holdingView.post(runnable);
        }
    }

    public void setHoldingView(BaseGLSurfaceView holdingView) {
        this.holdingView = holdingView;
    }

    public BaseGLSurfaceView getHoldingView() {
        return holdingView;
    }

    public void setViewRoot(ViewRoot viewRoot) {
        this.viewRoot = viewRoot;
    }

    public ViewRoot getViewRoot() {
        return viewRoot;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public Thread getMainThread() {
        return mainThread;
    }

    public void setUiLooper(UILooper uiLooper) {
        this.uiLooper = uiLooper;
    }

    public UILooper getUiLooper() {
        return uiLooper;
    }

    public int currentUIStep() {
        return getUiLooper().getStep();
    }

    /**
     * 在主线程上运行，当当前就是在HANDLE_RUNNABLES时立马执行
     */
    public void runOnUIThread(Runnable r) {
        if (currentUIStep() == UIStep.HANDLE_RUNNABLES) {
            r.run();
        } else {
            getUiLooper().post(r);
        }
    }

    public void runOnUIThread(Runnable r, double delayMs) {
        getUiLooper().post(r, delayMs);
    }

	/*
	public void setContent(EdView content) {
		this.content=content;
	}

	public EdView getContent() {
		return content;
	}
	*/

	/*
	public void setStep(int step) {
		this.step=step;
	}

	public int getStep() {
		return step;
	}*/

    private IRunnableHandler getRunnableHandler() {
        return uiLooper;
    }

    public void setDisplaySize(int w, int h) {
        this.displayWidth = w;
        this.displayHeight = h;
    }

    public void initial() {
        mainThread = Thread.currentThread();
        assetResource = new ApplicationAssetResource(getNativeContext().getAssets());
        CompileRawStringStore.load(getAssetResource().getShaderResource().subResource("store"));
    }

    public boolean checkThread() {
        return Thread.currentThread() == mainThread;
    }

    public Context getNativeContext() {
        return androidContext;
    }

    public ApplicationAssetResource getAssetResource() {
        return assetResource;
    }

    public double getFrameDeltaTime() {
        return uiLooper.getTimer().getDeltaTime();
    }
}
