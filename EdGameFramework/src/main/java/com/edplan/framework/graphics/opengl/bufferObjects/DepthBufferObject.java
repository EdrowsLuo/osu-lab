package com.edplan.framework.graphics.opengl.bufferObjects;

import android.opengl.GLES20;

import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.graphics.opengl.GLWrapped;

public class DepthBufferObject {
    private int bufferId;

    public int getBufferId() {
        return bufferId;
    }

    private void allocateMemory(int txW, int txH) {
        checkCurrent();
        GLES20.glRenderbufferStorage(
                GLES20.GL_RENDERBUFFER,
                GLES20.GL_DEPTH_COMPONENT16,
                txW, txH);
    }

    public static DepthBufferObject create(int width, int height) {
        DepthBufferObject dbo = genOne();
        dbo.bind();
        dbo.allocateMemory(width, height);
        dbo.unBind();
        return dbo;
    }

    private static DepthBufferObject genOne() {
        DepthBufferObject dbo = new DepthBufferObject();
        int[] id = new int[1];
        GLES20.glGenRenderbuffers(1, id, 0);
        dbo.bufferId = id[0];
        return dbo;
    }

    public void clear() {
        checkCurrent();
        GLWrapped.clearDepthBuffer();
    }

    /**
     * Delete can be called only the obj is unbind
     */
    public void delete() {
        GLES20.glDeleteRenderbuffers(1, new int[]{getBufferId()}, 0);
    }

    //-----for-bindable---

    private DepthBufferObject previousBuffer;

    private boolean binded = false;

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
            if (currentBuffer() == this) {
                throw new GLException("You bind a binded obj!! Maybe you forget to call this.unbind() ");
            } else {
                setBind(true);
                previousBuffer = currentBuffer();
                setCurrentBuffer(this);
            }
        }
    }

    public void unBind() {
        if (isBind()) {
            setBind(false);
            checkCurrent();
            setCurrentBuffer(previousBuffer);
        } else {
            throw new GLException("You unbind a unbind obj!! Maybe you forget to call this.bind() ");
        }

    }

    private void checkCurrent() {
        if (currentBuffer() != this) {
            throw new GLException(
                    "Current DepthBuffer isn't this!! Please operate BufferObject between bind() and unbind().Or another obj hasn't call unbind()");
        }
    }

    public static DepthBufferObject CURRENT_DEPTH_BUFFER;

    public static DepthBufferObject currentBuffer() {
        return CURRENT_DEPTH_BUFFER;
    }

    private static void setCurrentBuffer(DepthBufferObject fbo) {
        CURRENT_DEPTH_BUFFER = fbo;
        bind(fbo.getBufferId());
    }

    private static void bind(int id) {
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, id);
    }

    private static class SystemDepthBuffer extends DepthBufferObject {

        @Override
        public boolean isBind() {

            return true;
        }

        @Override
        public int getBufferId() {

            return 0;
        }

        @Override
        public void bind() {


        }

        @Override
        public void unBind() {

            //解绑根FBO不会进行任何操作
        }

        @Override
        public void delete() {

            //super.delete();
        }
    }

    static {
        CURRENT_DEPTH_BUFFER = new SystemDepthBuffer();
    }
}
