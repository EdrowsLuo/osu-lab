package com.edplan.framework.graphics.layer;

import com.edplan.framework.graphics.opengl.bufferObjects.FrameBufferObject;
import com.edplan.framework.graphics.opengl.drawui.GLDrawable;
import com.edplan.framework.graphics.opengl.drawui.DrawInfo;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.bufferObjects.FBOPool;
import com.edplan.framework.math.Mat4;

import android.util.Log;

import java.io.BufferedReader;

/**
 * 通过FBO，完全分离的绘制，最后的结果是一个Texture
 */
public class BufferedLayer {
    public static FBOPool DEF_FBOPOOL = new FBOPool();

    private FBOPool bufferedPool;

    private MContext context;

    private FrameBufferObject frameBuffer;

    private boolean hasDepthBuffer;

    private int width;

    private int height;

    private int ox = 0;

    private int oy = 0;

    private boolean permissionToTexture = true;

    private boolean joinPool = true;

    private boolean directLayer = false;


    /**
     * 分离一个direct layer出来
     *
     * @param layer
     */
    public BufferedLayer(BufferedLayer layer, int ox, int oy, int width, int height) {
        this.context = layer.context;
        this.directLayer = true;
        this.frameBuffer = layer.frameBuffer;
        this.height = height;
        this.width = width;
        if (layer.isDirectLayer()) {
            this.ox = ox + layer.ox;
            this.oy = oy + layer.oy;
        } else {
            this.ox = ox;
            this.oy = oy;
        }

        this.permissionToTexture = false;
    }

    public BufferedLayer(MContext context, int width, int height, boolean hasDepthBuffer) {
        this.context = context;
        this.width = width;
        this.height = height;
        this.hasDepthBuffer = hasDepthBuffer;
        bufferedPool = DEF_FBOPOOL;
    }

    public BufferedLayer(MContext context) {
        this.context = context;
        this.width = 1;
        this.height = 1;
        this.hasDepthBuffer = false;
        bufferedPool = DEF_FBOPOOL;
    }

    public BufferedLayer(MContext context, FrameBufferObject fbo) {
        this.context = context;
        this.frameBuffer = fbo;
        this.width = fbo.getWidth();
        this.height = fbo.getHeight();
        this.hasDepthBuffer = fbo.hasDepthAttachment();
        bufferedPool = DEF_FBOPOOL;
    }

    public BufferedLayer(MContext context, GLTexture texture, boolean useDepth) {
        this(context, FrameBufferObject.create(texture, useDepth));
        permissionToTexture = false;
    }

    public boolean isDirectLayer() {
        return directLayer;
    }

    public void setJoinPool(boolean joinPool) {
        this.joinPool = joinPool;
    }

    public boolean isJoinPool() {
        return joinPool;
    }

    public MContext getContext() {
        return context;
    }

    public void checkBind(String msg, boolean c) {
        if (isBind() != c) {
            throw new GLException("err operation:you can't operate layer when it is bind. msg: " + msg);
        }
    }

    public void setFrameBuffer(FrameBufferObject frameBuffer) {
        checkBind("setFrameBuffer", false);
        if (!isDirectLayer()) {
            this.frameBuffer = frameBuffer;
        }
    }

    public FrameBufferObject getFrameBuffer() {
        return frameBuffer;
    }

    public boolean isBind() {
        return (getFrameBuffer() != null) ? getFrameBuffer().isBind() : false;
    }

    public void setWidth(int width) {
        checkBind("setWidth", false);
        if (isDirectLayer()) {
            throw new GLException("改变direct layer是不允许的");
        }
        if (width <= 0) width = 1;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        checkBind("setHeight", false);
        if (isDirectLayer()) {
            throw new GLException("改变direct layer是不允许的");
        }
        if (height <= 0) height = 1;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public Mat4 createProjMatrix() {
        Mat4 projMatrix = new Mat4();
        projMatrix.setOrtho(0, getWidth(), 0, getHeight(), -100, 100);
        return projMatrix;
    }

    public void reCreateBuffer() {
        //Log.v("fbo-test","start reCreateBuffer");
        if (isDirectLayer()) {
            return;
        }
        if (frameBuffer != null) {
            if (frameBuffer.getCreatedHeight() >= height && frameBuffer.getCreatedWidth() >= width) {
                frameBuffer.setBound(width, height);
                //Log.v("fbo-test","1 resize fbo "+frameBuffer.getFBOId());
            } else {
                if (joinPool && !bufferedPool.saveFBO(frameBuffer)) {
                    frameBuffer.deleteWithAttachment();
                }//else Log.v("fbo-test","save fbo to pool "+frameBuffer.getFBOId());
                if (joinPool) frameBuffer = bufferedPool.requireFBO(width, height);
                if (frameBuffer == null) {
                    frameBuffer = FrameBufferObject.create(width, height, hasDepthBuffer);
                    //frameBuffer.setWidth(width);
                    //frameBuffer.setHeight(height);
                    //Log.v("fbo-test","1 create fbo "+frameBuffer.getFBOId());
                }//else Log.v("fbo-test","1 load fbo from pool "+frameBuffer.getFBOId());
            }
        } else {
            if (joinPool) frameBuffer = bufferedPool.requireFBO(width, height);
            if (frameBuffer == null) {
                frameBuffer = FrameBufferObject.create(width, height, hasDepthBuffer);
                //Log.v("fbo-test","2 create fbo "+frameBuffer.getFBOId());
            } else {
                //Log.v("fbo-test","2 load fbo from pool "+frameBuffer.getFBOId());
            }
        }
    }

    public void checkChange() {
        if (frameBuffer != null && frameBuffer.isBind()) {
            throw new RuntimeException("you can only check BufferedLayer when it is unbind");
        }
        if (frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height || (hasDepthBuffer != frameBuffer.hasDepthAttachment())) {
            reCreateBuffer();
        }
    }

    public AbstractTexture getTexture() {
        //checkChange();
        return frameBuffer != null ? frameBuffer.getTexture() : null;
    }

    public void bind() {
        checkChange();
        if (!getFrameBuffer().isBind()) {
            getFrameBuffer().bind();
        }
        GLWrapped.setViewport(ox, oy, width, height);
    }

    public void unbind() {
        if (getFrameBuffer().isBind()) {
            getFrameBuffer().unBind();
        }
    }

    boolean recycled = false;

    public void recycle() {
        if (recycled || isDirectLayer()) {
            return;
        } else {
            if (permissionToTexture) {
                if (joinPool) if (frameBuffer != null && !bufferedPool.saveFBO(frameBuffer))
                    frameBuffer.deleteWithAttachment();
            } else {
                frameBuffer.deleteWithAttachment();
            }
            recycled = true;
        }
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();
        recycle();
    }
}

