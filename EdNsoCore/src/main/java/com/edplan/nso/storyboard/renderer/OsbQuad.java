package com.edplan.nso.storyboard.renderer;

import com.edplan.framework.graphics.opengl.fast.FastRenderer;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Quad;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.nso.storyboard.elements.TypedCommand;
import com.edplan.nso.storyboard.renderer.OsbRenderer.*;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.animation.Easing;

public class OsbQuad {
    private final OsbVertex[] vertexs = new OsbVertex[4];

    public IMainFrameHandler<Color4> Color = new IMainFrameHandler<Color4>() {
        @Override
        public void update(Color4 startValue, Color4 endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.Color.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public IFloatMainFrameHandler PositionX = new IFloatMainFrameHandler() {
        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.PositionX.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public IFloatMainFrameHandler PositionY = new IFloatMainFrameHandler() {
        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.PositionY.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public IFloatMainFrameHandler ScaleX = new IFloatMainFrameHandler() {
        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.ScaleX.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public IFloatMainFrameHandler ScaleY = new IFloatMainFrameHandler() {
        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.ScaleY.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public IFloatMainFrameHandler Rotation = new IFloatMainFrameHandler() {
        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.Rotation.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public IFloatMainFrameHandler Alpha = new IFloatMainFrameHandler() {
        @Override
        public void update(float startValue, float endValue, double startTime, double duration, Easing easing) {

            for (OsbVertex v : vertexs) {
                v.Alpha.update(startValue, endValue, startTime, duration, easing);
            }
        }
    };

    public void load(OsbRenderer r) {
        r.getNextVertexs(vertexs);
    }

    public void updateAnchorOffset(AbstractTexture texture, Anchor anchor) {
        final float l = -texture.getWidth() * anchor.x();
        final float r = l + texture.getWidth();
        final float t = -texture.getHeight() * anchor.y();
        final float b = t + texture.getHeight();
        vertexs[0].AnchorOffset.set(l, t);
        vertexs[1].AnchorOffset.set(r, t);
        vertexs[2].AnchorOffset.set(l, b);
        vertexs[3].AnchorOffset.set(r, b);
    }

    public void updateTextureCoord(IQuad q, boolean flipH, boolean flipV) {
        if (flipH) {
            if (flipV) {
                vertexs[3].TextureCoord.set(q.getTopLeft());
                vertexs[2].TextureCoord.set(q.getTopRight());
                vertexs[1].TextureCoord.set(q.getBottomLeft());
                vertexs[0].TextureCoord.set(q.getBottomRight());
            } else {
                vertexs[1].TextureCoord.set(q.getTopLeft());
                vertexs[0].TextureCoord.set(q.getTopRight());
                vertexs[3].TextureCoord.set(q.getBottomLeft());
                vertexs[2].TextureCoord.set(q.getBottomRight());
            }
        } else {
            if (flipV) {
                vertexs[2].TextureCoord.set(q.getTopLeft());
                vertexs[3].TextureCoord.set(q.getTopRight());
                vertexs[0].TextureCoord.set(q.getBottomLeft());
                vertexs[1].TextureCoord.set(q.getBottomRight());
            } else {
                vertexs[0].TextureCoord.set(q.getTopLeft());
                vertexs[1].TextureCoord.set(q.getTopRight());
                vertexs[2].TextureCoord.set(q.getBottomLeft());
                vertexs[3].TextureCoord.set(q.getBottomRight());
            }
        }
    }


    public void addToRenderer(OsbRenderer r) {
        r.addIndices(
                vertexs[0].index,
                vertexs[1].index,
                vertexs[2].index,
                vertexs[1].index,
                vertexs[3].index,
                vertexs[2].index
        );
    }

    public void dispos() {
        for (int i = 0; i < vertexs.length; i++) {
            if (vertexs[i] != null) vertexs[i].loadback();
            vertexs[i] = null;
        }
    }
}
