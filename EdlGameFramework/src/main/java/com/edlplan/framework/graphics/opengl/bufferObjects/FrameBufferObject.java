package com.edlplan.framework.graphics.opengl.bufferObjects;

import android.opengl.GLES20;
import android.util.Log;

import com.edlplan.framework.graphics.opengl.GLException;
import com.edlplan.framework.graphics.opengl.GLWrapped;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.graphics.opengl.objs.texture.TextureRegion;
import com.edlplan.framework.math.RectF;

public class FrameBufferObject {

    private boolean isDepthAttached = true;
    private DepthBufferObject depthAttachment;

    private boolean permissionToDeleteTexture = true;
    private GLTexture colorAttachment;

    private TextureRegion region;

    private int frameBufferId;

    private int createdWidth;

    private int createdHeight;

    private int width;

    private int height;

    private boolean deleted = false;

    FrameBufferObject() {

    }

    FrameBufferObject(int width, int height) {
        this.width = width;
        this.height = height;
        this.createdHeight = height;
        this.createdWidth = width;
    }

    public boolean isDepthBufferAttached() {
        return depthAttachment != null && isDepthAttached;
    }

    public void attacheDepthBuffer() {
        checkCurrent();
        if (depthAttachment == null) {
            linkDepthBuffer(DepthBufferObject.create(colorAttachment.getWidth(), colorAttachment.getHeight()));
        } else {
            if (!isDepthAttached) {
                isDepthAttached = true;
                GLES20.glFramebufferRenderbuffer(
                        GLES20.GL_FRAMEBUFFER,
                        GLES20.GL_DEPTH_ATTACHMENT,
                        GLES20.GL_RENDERBUFFER,
                        depthAttachment.getBufferId()
                );
            }
        }
    }

    public void dettachDepthBuffer() {
        checkCurrent();
        if (depthAttachment != null) {
            if (isDepthAttached) {
                isDepthAttached = false;
                GLES20.glFramebufferRenderbuffer(
                        GLES20.GL_FRAMEBUFFER,
                        GLES20.GL_DEPTH_ATTACHMENT,
                        GLES20.GL_RENDERBUFFER,
                        0
                );
            }
        }
    }

    protected void setCreatedWidth(int createdWidth) {
        this.createdWidth = createdWidth;
    }

    public int getCreatedWidth() {
        return createdWidth;
    }

    protected void setCreatedHeight(int createdHeight) {
        this.createdHeight = createdHeight;
    }

    public int getCreatedHeight() {
        return createdHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBound(int w, int h) {
        width = w;
        height = h;
        region.resize(w, h);
    }
	
	/*
	public void setWidth(int w){
		width=w;
		if(region!=null)region.setWidth(w);
	}
	
	public void setHeight(int h){
		height=h;
		if(region!=null)region.setHeight(h);
	}
	*/

    public void clearDepthBuffer() {
        checkCurrent();
        if (hasDepthAttachment()) {
            getDepthAttachment().clear();
        }
    }

    private void setDepthAttachment(DepthBufferObject depthAttachment) {
        this.depthAttachment = depthAttachment;
    }

    public DepthBufferObject getDepthAttachment() {
        return depthAttachment;
    }

    public boolean hasDepthAttachment() {
        return getDepthAttachment() != null;
    }

    private void setColorAttachment(GLTexture colorAttachment) {
        this.colorAttachment = colorAttachment;
    }

    public AbstractTexture getTexture() {
        if (region == null) Log.w("err-gl", "region is null : " + hasLinkRegion);
        return region;
    }

    private GLTexture getColorAttachment() {
        return colorAttachment;
    }

    public boolean hasColorAttachment() {
        return getColorAttachment() != null;
    }

    public int getFBOId() {
        return frameBufferId;
    }

    boolean hasLinkRegion = false;

    private void linkColorAttachment(GLTexture texture) {
        checkCurrent();
        if (colorAttachment != null) {
            throw new GLException("A FrameBufferObject can't attach two Texture...Maybe support future:(");
        } else {
            setColorAttachment(texture);
            region = new TextureRegion(texture, new RectF(0, 0, texture.getWidth(), texture.getHeight()));
            hasLinkRegion = true;
            GLES20.glFramebufferTexture2D(
                    GLES20.GL_FRAMEBUFFER,
                    GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D,
                    texture.getTextureId(),
                    0
            );
        }
    }

    private void linkDepthBuffer(DepthBufferObject dbo) {
        checkCurrent();
        if (depthAttachment != null) {
            throw new GLException("A FrameBufferObject can't attach two DepthBuffer");
        } else {
            setDepthAttachment(dbo);
            GLES20.glFramebufferRenderbuffer(
                    GLES20.GL_FRAMEBUFFER,
                    GLES20.GL_DEPTH_ATTACHMENT,
                    GLES20.GL_RENDERBUFFER,
                    dbo.getBufferId()
            );
        }
    }

    public void delete() {
        if (!deleted) {
            deleted = true;
            GLES20.glDeleteFramebuffers(1, new int[]{getFBOId()}, 0);
        }
    }

    public void deleteWithAttachment() {
        if (hasColorAttachment() && permissionToDeleteTexture) {
            getColorAttachment().delete();
        }
        if (hasDepthAttachment()) {
            getDepthAttachment().delete();
        }
        this.delete();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        delete();
    }

    public static FrameBufferObject create(GLTexture texture, boolean needDepth) {
        FrameBufferObject fbo = createFBO();
        fbo.permissionToDeleteTexture = false;
        fbo.width = texture.getWidth();
        fbo.height = texture.getHeight();
        fbo.setCreatedHeight(texture.getWidth());
        fbo.setCreatedWidth(texture.getHeight());
        fbo.bind();
        //if(useDepth){
        if (needDepth)
            fbo.linkDepthBuffer(DepthBufferObject.create(texture.getWidth(), texture.getHeight()));
        //}
        fbo.linkColorAttachment(GLTexture.createGPUTexture(texture.getWidth(), texture.getHeight()));
        fbo.unBind();
        return fbo;
    }

    public static FrameBufferObject create(int width, int height, boolean useDepth) {
        FrameBufferObject fbo = createFBO();
        fbo.width = width;
        fbo.height = height;
        fbo.setCreatedHeight(height);
        fbo.setCreatedWidth(width);
        fbo.bind();
        if (useDepth) {
            fbo.linkDepthBuffer(DepthBufferObject.create(width, height));
        }
        fbo.linkColorAttachment(GLTexture.createGPUTexture(width, height));
        fbo.unBind();
        return fbo;
    }

    private static FrameBufferObject createFBO() {
        FrameBufferObject fbo = new FrameBufferObject();
        fbo.frameBufferId = genOne();
        return fbo;
    }


    private static int genOne() {
        return GLWrapped.genFrameBufferObject();
    }

    //-----for-bindable---

    private FrameBufferObject previousFBO;

    protected boolean binded = false;

    public boolean isBind() {
        return binded;
    }

    private void setBind(boolean b) {
        binded = b;
    }

    public void bind() {
        if (isBind()) {
            throw new GLException("You bind a binded obj!! Maybe you forget to call this.unbind() ");
        } else {
            if (currentFBO() == this) {
                throw new GLException("You bind a binded obj!! Maybe you forget to call this.unbind() ");
            } else {
                setBind(true);
                previousFBO = currentFBO();
                setCurrentFBO(this);
                //if(hasDepthAttachment()&&!getDepthAttachment().isBind()){
                //	getDepthAttachment().bind();
                //}
                //默认bind后自动将viewport设置为当前的
            }
        }
    }

    public void unBind() {
        if (isBind()) {
            setBind(false);
            checkCurrent();
            setCurrentFBO(previousFBO);
            previousFBO = null;
            if (hasDepthAttachment() && getDepthAttachment().isBind()) {
                getDepthAttachment().unBind();
            }
        } else {
            throw new GLException("You unbind a unbinded obj!! Maybe you forget to call this.bind() ");
        }

    }

    private void checkCurrent() {
        if (currentFBO() != this) {
            throw new GLException(
                    "Current FrameBufferObject isn't this!! Please operate FrameBufferObject between bind() and unbind().Or another fbo hasn't call unbind()");
        }
    }

    public static void bindInitial(SystemFrameBuffer sysFrameBuffer) {
        CURRENT_FRAMEBUFFER = sysFrameBuffer;
        GLWrapped.setViewport(0, 0, sysFrameBuffer.getWidth(), sysFrameBuffer.getHeight());
    }

    public static FrameBufferObject CURRENT_FRAMEBUFFER;

    public static FrameBufferObject currentFBO() {
        return CURRENT_FRAMEBUFFER;
    }

    private static void setCurrentFBO(FrameBufferObject fbo) {
        CURRENT_FRAMEBUFFER = fbo;
        if (fbo.region != null) {
            GLWrapped.setViewport((int) fbo.region.getArea().getLeft(), (int) fbo.region.getArea().getTop(), fbo.getWidth(), fbo.getHeight());
        } else {
            GLWrapped.setViewport(0, 0, fbo.getWidth(), fbo.getHeight());
        }
        bind(fbo.getFBOId());
    }

    private static void bind(int id) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, id);
    }

    public static class SystemFrameBuffer extends FrameBufferObject {

        private static final int SYSTEM_FRAMEBUFFER_ID = 0;

        public SystemFrameBuffer(int width, int height) {
            super(width, height);
        }

        @Override
        public AbstractTexture getTexture() {

            Log.w("err-gl", "you called getTexture on SystemBuffer");
            throw new RuntimeException("you called getTexture on SystemBuffer");
            //return super.getTexture();
        }

        @Override
        public boolean hasDepthAttachment() {

            return true;
        }

        @Override
        public int getFBOId() {

            return SYSTEM_FRAMEBUFFER_ID;
        }

        @Override
        public void bind() {

            binded = true;
            bindInitial(this);
            FrameBufferObject.bind(SYSTEM_FRAMEBUFFER_ID);
        }

        @Override
        public void unBind() {

            //解绑根FBO不会进行任何操作
            this.binded = false;
        }

        @Override
        public void delete() {

            //super.delete();
        }
    }


}
