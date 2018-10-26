package com.edplan.framework.graphics.opengl;

import android.opengl.GLES20;
import android.util.Log;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.utils.AbstractSRable;
import com.edplan.framework.utils.SRable;
import com.edplan.framework.utils.SRable.SROperation;
import com.edplan.framework.utils.advance.BooleanCopyable;
import com.edplan.framework.utils.advance.BooleanSetting;
import com.edplan.framework.interfaces.Setter;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.interfaces.Copyable;
import com.edplan.framework.graphics.opengl.bufferObjects.FBOPool;

import android.opengl.GLES10;
import android.opengl.GLES30;
import android.opengl.GLES31Ext;
import android.opengl.GLES11Ext;

import java.nio.Buffer;
import java.util.Stack;

public class GLWrapped {
    public static int GL_VERSION;

    public static int GL_SHORT = GLES20.GL_SHORT;

    public static int GL_UNSIGNED_SHORT = GLES20.GL_UNSIGNED_SHORT;

    public static int GL_TRIANGLES = GLES20.GL_TRIANGLES;

    public static int GL_MAX_TEXTURE_SIZE;

    private static boolean enable = true;

    public static final
    BooleanSetting depthTest = new BooleanSetting(new Setter<Boolean>() {
        @Override
        public void set(Boolean t) {

            if (t) {
                if (GL_VERSION == 2) {
                    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                } else {
                    GLES10.glEnable(GLES10.GL_DEPTH_TEST);
                }
            } else {
                if (GL_VERSION == 2) {
                    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                } else {
                    GLES10.glDisable(GLES10.GL_DEPTH_TEST);
                }
            }
        }
    },
            false).initial();

    public static BlendSetting blend = new BlendSetting().setUp();

    public static void setEnable(boolean enable) {
        GLWrapped.enable = enable;
    }

    public static boolean isEnable() {
        return enable;
    }

    public static void initial(int version) {
        GL_VERSION = version;
        GL_MAX_TEXTURE_SIZE = getIntegerValue(GLES20.GL_MAX_TEXTURE_SIZE);
        GLTexture.initial();
        FBOPool.initialGL();
        //GLES20.glEnable(GLES20.gl_stencil_t
        //GLES20.glst
    }

    public static void onFrame() {
        drawCalls = 0;
        fboCreate = 0;
    }

    private static int drawCalls = 0;

    private static int fboCreate = 0;

    public static void drawArrays(int mode, int offset, int count) {
        if (enable) GLES20.glDrawArrays(mode, offset, count);
        drawCalls++;
    }

    public static void drawElements(int mode, int count, int type, Buffer b) {
        if (enable) GLES20.glDrawElements(mode, count, type, b);
        drawCalls++;
    }

    public static int frameDrawCalls() {
        return drawCalls;
    }

    private static int px1, pw, py1, ph;

    public static void setViewport(int x1, int y1, int w, int h) {
        //if(!(px1==x1&&px2==x2&&py1==y1&&py2==y2)){
        if (GL_VERSION == 2) {
            GLES20.glViewport(x1, y1, w, h);
        } else {
            GLES10.glViewport(x1, y1, w, h);
        }
        px1 = x1;
        pw = w;
        py1 = y1;
        ph = h;
        //}
    }

    public static void setClearColor(float r, float g, float b, float a) {
        if (GL_VERSION == 2) {
            GLES20.glClearColor(r, g, b, a);
        } else {
            GLES10.glClearColor(r, g, b, a);
        }
    }

    public static void clearColorBuffer() {
        if (enable) GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public static void clearDepthBuffer() {
        if (enable) GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
    }

    public static void clearDepthAndColorBuffer() {
        if (enable) GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
	
	/*
	public static void setDepthTest(boolean f){
		if(f!=depthTest){
			if(f){
				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			}else{
				GLES20.glDisable(GLES20.GL_DEPTH_TEST);
			}
			depthTest=f;
		}
	}*/

    public static void setClearColor(Color4 c) {
        setClearColor(c.r, c.g, c.b, c.a);
    }


    public static int genFrameBufferObject() {
        int[] i = new int[1];
        GLES20.glGenFramebuffers(1, i, 0);
        fboCreate++;
        return i[0];
    }

    public static int getFboCreate() {
        return fboCreate;
    }

    public static int getIntegerValue(int key) {
        if (GL_VERSION >= 2) {
            int[] b = new int[1];
            GLES20.glGetIntegerv(key, b, 0);
            return b[0];
        } else {
            int[] b = new int[1];
            GLES10.glGetIntegerv(key, b, 0);
            return b[0];
        }
    }

    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new GLException(op + ": glError " + error);
        }
    }

    private static Stack<BaseCanvas> canvasStack = new Stack<>();

    protected static void prepareCanvas(BaseCanvas canvas) {
        if (!canvasStack.empty()) {
            final BaseCanvas pre = canvasStack.peek();
            if (pre.isPrepared()) {
                //忘记释放，这里就帮忙释放
                pre.onUnprepare();
            }
        }
        canvasStack.push(canvas);
        canvas.onPrepare();
    }

    protected static void unprepareCanvas(BaseCanvas canvas) {
        if (canvasStack.empty() || canvasStack.peek() != canvas) {
            //发生错误，释放的画板不是当前画板
            throw new GLException("错误的canvas释放顺序！");
        }
        canvas.onUnprepare();
        canvasStack.pop();
        if (!canvasStack.empty()) {
            canvasStack.peek().onPrepare();
        }
    }
}
