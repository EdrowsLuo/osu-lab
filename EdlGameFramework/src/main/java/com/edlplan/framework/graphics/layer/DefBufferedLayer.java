package com.edlplan.framework.graphics.layer;

import com.edlplan.framework.graphics.opengl.GLException;
import com.edlplan.framework.graphics.opengl.bufferObjects.FrameBufferObject;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.MContext;

public class DefBufferedLayer extends BufferedLayer {
    public DefBufferedLayer(MContext con, int width, int height) {
        super(con, new FrameBufferObject.SystemFrameBuffer(width, height));
    }

    public void prepare() {
        bind();
        unbind();
    }

    @Override
    public void reCreateBuffer() {
        //you can't recreate SystemBuffer
        return;
    }

    private void err() {
        throw new GLException("err operation on sys_bufferedLayer");
    }

    @Override
    public AbstractTexture getTexture() {

        return super.getTexture();
    }

    @Override
    public FrameBufferObject getFrameBuffer() {

        return super.getFrameBuffer();
    }


    @Override
    public void setHeight(int height) {

        err();
        super.setHeight(height);
    }

    @Override
    public void setFrameBuffer(FrameBufferObject frameBuffer) {

        err();
        super.setFrameBuffer(frameBuffer);
    }

    @Override
    public void setWidth(int width) {

        err();
        super.setWidth(width);
    }

    @Override
    public void recycle() {
        //recycle a SystemBuffer will happen no thing
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
