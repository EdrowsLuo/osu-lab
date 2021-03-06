package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.math.IQuad;

import java.nio.FloatBuffer;

public abstract class BaseRectTextureSprite<S extends TextureSpriteShader> extends RectSprite<S> {
    private AbstractTexture texture = GLTexture.White;

    private FloatBuffer textureCoordBuffer;

    public BaseRectTextureSprite(MContext c) {
        super(c);
        textureCoordBuffer = BufferUtil.createFloatBuffer(2 * 4);
    }

    public void setTexture(AbstractTexture t) {
        setTexture(t, null);
    }

    public void setTexture(AbstractTexture texture, IQuad area) {
        this.texture = texture;
        if (area == null) {
            area = texture.getRawQuad();
        } else {
            area = texture.toTextureQuad(area);
        }
        textureCoordBuffer.position(0);
        area.getBottomLeft().put2buffer(textureCoordBuffer);
        area.getBottomRight().put2buffer(textureCoordBuffer);
        area.getTopRight().put2buffer(textureCoordBuffer);
        area.getTopLeft().put2buffer(textureCoordBuffer);
        textureCoordBuffer.position(0);
    }

    public AbstractTexture getTexture() {
        return texture;
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {

        super.prepareShader(canvas);
        getShader().loadTexture(texture);
    }

    @Override
    protected void loadVertexs(BaseCanvas canvas) {

        super.loadVertexs(canvas);
        getShader().loadTextureCoord(textureCoordBuffer);
    }
}
