package com.edplan.framework.graphics.opengl.batch.v2.object;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;

import java.util.List;

public class MultipleTextureQuad extends TextureQuad {

    public TextureEntry[] textureEntries;

    public TextureEntry currentEntry;

    public void switchTexture(int id) {
        if (textureEntries.length == 0) {
            throw new RuntimeException();
        }
        id = id % textureEntries.length;
        currentEntry = textureEntries[id];
        texture = currentEntry.texture;
        size = currentEntry.size;
        u1 = currentEntry.u1;
        v1 = currentEntry.v1;
        u2 = currentEntry.u2;
        v2 = currentEntry.v2;
    }

    public void initialWithTextureList(List<AbstractTexture> textures) {

        textureEntries = new TextureEntry[textures.size()];
        if (textureEntries.length == 0) {
            return;
        }

        for (int i = 0; i < textureEntries.length; i++) {
            AbstractTexture texture = textures.get(i);
            TextureEntry entry = new TextureEntry();
            entry.texture = texture;
            entry.size = new Vec2(texture.getWidth(), texture.getHeight());
            RectF rectF = texture.toTextureRect(0, 0, texture.getWidth(), texture.getHeight());
            entry.u1 = rectF.getLeft();
            entry.u2 = rectF.getRight();
            entry.v1 = rectF.getTop();
            entry.v2 = rectF.getBottom();
            textureEntries[i] = entry;
        }

        switchTexture(0);
    }

    public void initialWithTextureListWithScale(List<AbstractTexture> textures, float globalScale) {

        textureEntries = new TextureEntry[textures.size()];
        if (textureEntries.length == 0) {
            return;
        }

        for (int i = 0; i < textureEntries.length; i++) {
            AbstractTexture texture = textures.get(i);
            TextureEntry entry = new TextureEntry();
            entry.texture = texture;
            entry.size = new Vec2(texture.getWidth() * globalScale, texture.getHeight() * globalScale);
            RectF rectF = texture.toTextureRect(0, 0, texture.getWidth(), texture.getHeight());
            entry.u1 = rectF.getLeft();
            entry.u2 = rectF.getRight();
            entry.v1 = rectF.getTop();
            entry.v2 = rectF.getBottom();
            textureEntries[i] = entry;
        }

        switchTexture(0);
    }

    public class TextureEntry {

        public Vec2 size = new Vec2();

        public AbstractTexture texture;

        public float u1,v1,u2, v2;

    }

}
