package com.edplan.framework.graphics.opengl.objs.texture;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Vec2;

import java.util.ArrayList;

import com.edplan.framework.math.IQuad;

public class FrameTexture extends AbstractTexture {
    private ArrayList<AbstractTexture> textures = new ArrayList<AbstractTexture>();

    private int currentIndex = 0;

    public void setTextures(ArrayList<AbstractTexture> textures) {
        this.textures = textures;
    }

    public ArrayList<AbstractTexture> getTextures() {
        return textures;
    }

    public int frameCount() {
        return textures.size();
    }

    public AbstractTexture currentFrameTexture() {
        return textures.get(currentIndex);
    }

    public void setFrame(int frameIndex) {
        currentIndex = frameIndex % frameCount();
    }

    public void addFrame(AbstractTexture texture) {
        textures.add(texture);
    }

    @Override
    public int getTextureId() {

        return getTexture().getTextureId();
    }

    @Override
    public GLTexture getTexture() {

        return currentFrameTexture().getTexture();
    }

    @Override
    public int getHeight() {

        return currentFrameTexture().getHeight();
    }

    @Override
    public int getWidth() {

        return currentFrameTexture().getWidth();
    }

    @Override
    public Vec2 toTexturePosition(float x, float y) {

        return currentFrameTexture().toTexturePosition(x, y);
    }

    @Override
    public IQuad getRawQuad() {

        return null;
    }

    /**
     * 返回一个FrameTexture，共享texures，不共享当前状态，
     * 对textures的改变会应用到返回的FrameTexture上，setFrame则无效
     */
    public FrameTexture sharedInstance() {
        FrameTexture t = new FrameTexture();
        t.setTextures(textures);
        return t;
    }

    public static FrameTexture split(GLTexture texture, int row, int cro) {
        TextureRegion[][] l = TextureHelper.split(texture, row, cro);
        FrameTexture frameTexture = new FrameTexture();
        for (TextureRegion[] rl : l) {
            for (TextureRegion r : rl) {
                frameTexture.addFrame(r);
            }
        }
        return frameTexture;
    }
}
