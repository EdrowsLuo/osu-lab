package com.edlplan.framework.graphics.opengl.batch.v2.object;

import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.Anchor;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.FloatRef;

public class TextureQuad extends  ATextureQuad{

    public static final int TopLeft = 0;

    public static final int TopRight = 1;

    public static final int BottomLeft = 2;

    public static final int BottomRight = 3;

    public Vec2 size = new Vec2();

    public Vec2 position = new Vec2();

    public Anchor anchor = Anchor.Center;

    public Vec2 scale;

    public FloatRef rotation;

    public FloatRef alpha = new FloatRef(1);

    public Color4 accentColor;

    public float u1,v1,u2, v2;

    public TextureQuad syncPosition(Vec2 position) {
        this.position = position;
        return this;
    }

    public TextureQuad enableScale() {
        if (scale == null) {
            scale = new Vec2(1, 1);
        }
        return this;
    }

    public TextureQuad syncScale(Vec2 vec2) {
        scale = vec2;
        return this;
    }

    public TextureQuad enableColor() {
        if (accentColor == null) {
            accentColor = Color4.White.copyNew();
        }
        return this;
    }

    public TextureQuad syncColor(Color4 color) {
        accentColor = color;
        return this;
    }

    public TextureQuad syncAlpha(FloatRef ref) {
        this.alpha = ref;
        return this;
    }

    public TextureQuad enableRotation() {
        if (rotation == null) {
            rotation = new FloatRef();
        }
        return this;
    }

    public TextureQuad syncRotation(FloatRef ref) {
        this.rotation = ref;
        return this;
    }

    public void setTextureAndSize(AbstractTexture texture) {
        this.texture = texture;
        size.set(texture.getWidth(), texture.getHeight());
        RectF rectF = texture.toTextureRect(0, 0, texture.getWidth(), texture.getHeight());
        u1 = rectF.getLeft();
        u2 = rectF.getRight();
        v1 = rectF.getTop();
        v2 = rectF.getBottom();
    }

    public void setBaseWidth(float width) {
        size.set(width, width * (size.y / size.x));
    }

    public void setBaseHeight(float height) {
        size.set(height * (size.x / size.y), height);
    }

    @Override
    public void write(float[] ary, int offset) {
        float l = -size.x * anchor.x();
        float r = size.x + l;
        float t = -size.y * anchor.y();
        float b = size.y + t;
        float color = accentColor == null ? Color4.getPackedAlphaBit(alpha.value) : accentColor.toFloatBitABGR(alpha.value);

        if (scale != null) {
            l *= scale.x;
            r *= scale.x;
            t *= scale.y;
            b *= scale.y;
        }

        if (rotation == null) {
            l += position.x;
            r += position.x;
            t += position.y;
            b += position.y;
            ary[offset++] = l;
            ary[offset++] = t;
            ary[offset++] = u1;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = r;
            ary[offset++] = t;
            ary[offset++] = u2;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = l;
            ary[offset++] = b;
            ary[offset++] = u1;
            ary[offset++] = v2;
            ary[offset++] = color;


            ary[offset++] = r;
            ary[offset++] = b;
            ary[offset++] = u2;
            ary[offset++] = v2;
            ary[offset  ] = color;
        } else {
            final float s = (float) Math.sin(rotation.value);
            final float c = (float) Math.cos(rotation.value);
            final float x = position.x, y = position.y;
            ary[offset++] = l * c - t * s + x;
            ary[offset++] = l * s + t * c + y;
            ary[offset++] = u1;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = r * c - t * s + x;
            ary[offset++] = r * s + t * c + y;
            ary[offset++] = u2;
            ary[offset++] = v1;
            ary[offset++] = color;

            ary[offset++] = l * c - b * s + x;
            ary[offset++] = l * s + b * c + y;
            ary[offset++] = u1;
            ary[offset++] = v2;
            ary[offset++] = color;

            ary[offset++] = r * c - b * s + x;
            ary[offset++] = r * s + b * c + y;
            ary[offset++] = u2;
            ary[offset++] = v2;
            ary[offset  ] = color;
        }
    }
}
