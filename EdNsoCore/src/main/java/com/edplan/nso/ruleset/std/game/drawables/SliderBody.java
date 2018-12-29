package com.edplan.nso.ruleset.std.game.drawables;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.line.AbstractPath;
import com.edplan.framework.graphics.line.DrawLinePath;
import com.edplan.framework.graphics.line.LegacyDrawLinePath;
import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edplan.framework.graphics.opengl.batch.v2.object.PackedTriangles;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edplan.framework.ui.drawable.BufferedDrawable;
import com.edplan.framework.utils.FloatRef;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.paint.AdvancedDrawObject;

public class SliderBody extends AdvancedDrawObject {

    private LinePath path;

    private DrawLinePath drawLinePath;

    private float length;

    private boolean dirty = true;

    private float progress1 = 0;

    private float progress2 = 1;

    private Color4 accentColor = Color4.White.copyNew();

    public final FloatRef alpha = new FloatRef(1);

    private GLTexture pathTexture;

    public SliderBody(MContext context,AbstractPath path, float width, float length) {
        this.length = length;
        this.path = path.fitToLinePath();
        this.path.setWidth(width);
        this.path.measure();
        this.path.bufferLength(length);
    }

    public void setProgress1(float progress1) {
        if (Math.abs(progress1 - this.progress1) > 0.01) {
            dirty = true;
            this.progress1 = progress1;
        }
    }

    public void setProgress2(float progress2) {
        if (Math.abs(progress2 - this.progress2) > 0.01) {
            dirty = true;
            this.progress2 = progress2;
        }
    }

    private DrawLinePath getDrawLinePath() {
        if (dirty) {
            dirty = false;
            drawLinePath = new DrawLinePath(path.cutPath(length * progress1, length * progress2), alpha);
        }
        return drawLinePath;
    }

    public void setPath(LinePath path) {
        dirty = true;
        this.path = path;
    }

    public LinePath getPath() {
        return path;
    }

    private void initialPathTexture() {
        float aa_portion = 0.02f;
        float border_portion = 0.128f;
        float mix_width = 0.02f;
        float mix_start = border_portion - mix_width;
        float gradient_portion = 1 - border_portion + mix_width;

        float opacity_at_centre = 0.3f;
        float opacity_at_edge = 0.8f;

        Color4 centerColor = Color4.rgba(0.8f, 0.8f, 0.8f, 0.7f);
        Color4 borderColor = Color4.rgba(1, 1, 1, 1);
        Color4 sideColor = Color4.rgba(0.25f, 0.25f, 0.25f, 0.7f);

        Bitmap bmp = Bitmap.createBitmap(512, 1, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < bmp.getWidth(); x++) {
            float v = 1 - x / (float) (bmp.getWidth() - 1);

            if (v <= mix_start) {
                bmp.setPixel(x, 0,
                        borderColor
                                .copyNew()
                                .multiple(
                                        Color4.alphaMultipler(
                                                Math.min(v / aa_portion, 1)
                                        )
                                ).toIntBit());
                //Color4.gray(Math.min(v/aa_portion,1)).toIntBit());
                //Color.argb((int)(Math.min(v/aa_portion,1)*255),0,0,0));
            } else if (v <= border_portion) {
                Color4 c0 = borderColor.copyNew();
                float mix_rate = (v - mix_start) / mix_width;
                v -= border_portion;
                //float pro=opacity_at_edge-(opacity_at_edge-opacity_at_centre)*v/gradient_portion;
                Color4 c1 = Color4.mix(centerColor, sideColor, 1 - v / gradient_portion);
                bmp.setPixel(x, 0, Color4.mix(c0, c1, mix_rate).toIntBit());
            } else {
                v -= border_portion;
                bmp.setPixel(x, 0,
                        Color4.mix(centerColor, sideColor, 1 - v / gradient_portion).toIntBit()
                        //centerColor
                        //.copyNew()
                        //.multiple(
                        // Color4.alphaMultipler(

                        // )
                        //).toIntBit()
                );
                //Color4.gray(1-(opacity_at_edge-(opacity_at_edge-opacity_at_centre)*v/gradient_portion)).toIntBit());
                //Color.argb((int)((opacity_at_edge-(opacity_at_edge-opacity_at_centre)*v/gradient_portion)*255),0,0,0));
            }
        }

        pathTexture = GLTexture.create(bmp);
    }

    private GLTexture getPathTexture() {
        if (pathTexture == null) {
            initialPathTexture();
        }
        return pathTexture;
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {

        GLCanvas2D glCanvas2D = (GLCanvas2D) canvas;
        PackedTriangles triangles = getDrawLinePath().getTriangles();

        boolean isDepthEnable = glCanvas2D.getLayer().getFrameBuffer().isDepthBufferAttached();
        if (!isDepthEnable) {
            glCanvas2D.getLayer().getFrameBuffer().attacheDepthBuffer();
        }

        GLWrapped.depthTest.save();
        GLWrapped.blend.save();
        GLWrapped.depthTest.forceSet(true);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLWrapped.blend.setEnable(true);
        GLWrapped.clearDepthBuffer();

        //先绘制一遍将depth buffer填充一下
        triangles.render(
                Texture3DShader.DepthShader.DEFAULT.get(),
                GLTexture.White,
                BatchEngine.getShaderGlobals());

        //实际绘制滑条
        float a = BatchEngine.getShaderGlobals().alpha;
        BatchEngine.getShaderGlobals().alpha = alpha.value;

        triangles.render(
                Texture3DShader.DEFAULT.get(),
                getPathTexture(),
                BatchEngine.getShaderGlobals());

        BatchEngine.getShaderGlobals().alpha = a;

        GLWrapped.blend.restore();
        GLWrapped.depthTest.restore();

        if (!isDepthEnable) {
            glCanvas2D.getLayer().getFrameBuffer().dettachDepthBuffer();
        }

    }
}
