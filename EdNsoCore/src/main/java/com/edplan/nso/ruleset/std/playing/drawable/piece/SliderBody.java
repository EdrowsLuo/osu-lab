package com.edplan.nso.ruleset.std.playing.drawable.piece;

import android.graphics.Bitmap;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.line.LegacyDrawLinePath;
import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.drawable.BufferedDrawable;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.std.objects.StdSlider;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdSlider;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class SliderBody extends BasePiece {
    private BufferedDrawable sliderBuffered;

    private float progress1 = 0;

    private float progress2 = 0;

    private StdSlider slider;

    private LinePath sliderPath;

    public SliderBody(MContext c, PreciseTimeline t, DrawableStdSlider sld) {
        super(c, t);
        this.slider = (StdSlider) sld.getHitObject();
        sliderPath = sld.getPath();
    }

    public void applyStackOffset(float offset) {
		/*
		sliderPath.translate(offset,offset);
		sliderPath.measure();
		sliderPath.bufferLength((float)slider.getPixelLength());
		if(sliderBuffered!=null){
			sliderBuffered.setArea(sliderPath.getDrawArea());
			sliderBuffered.postUpdate();
		}
		*/
    }

    public Vec2 getCurrentHeadPoint() {
        return sliderPath.getMeasurer().atLength((float) (slider.getPixelLength() * getProgress1()));
    }

    public Vec2 getCurrentEndPoint() {
        return sliderPath.getMeasurer().atLength((float) (slider.getPixelLength() * getProgress2()));
    }

    public Vec2 getPointAt(float length) {
        return sliderPath.getMeasurer().atLength(length);
    }

    @Override
    public void setAlpha(float a) {

        super.setAlpha(a);
        if (sliderBuffered != null) sliderBuffered.setAlpha(a);
    }

    public LinePath getSliderPath() {
        return sliderPath;
    }

    @Override
    public void setSkin(OsuSkin skin) {

        super.setSkin(skin);
        sliderBuffered = new BufferedSliderDrawable(getContext());
        sliderBuffered.setArea(sliderPath.getDrawArea());
    }

    public void setProgress1(float progress1) {
        if (this.progress1 != progress1) {
            this.progress1 = progress1;
            sliderBuffered.postUpdate();
        }
    }

    public float getProgress1() {
        return progress1;
    }

    public void setProgress2(float progress2) {
        if (this.progress2 != progress2) {
            this.progress2 = progress2;
            sliderBuffered.postUpdate();
        }
    }

    public float getProgress2() {
        return progress2;
    }

    @Override
    public void onFinish() {

        super.onFinish();
        sliderBuffered.recycle();
    }

    @Override
    public void draw(BaseCanvas canvas) {

        if (!isFinished()) sliderBuffered.draw(canvas);
    }

    public class BufferedSliderDrawable extends BufferedDrawable {

        private Texture3DBatch<TextureVertex3D> batch = new Texture3DBatch<TextureVertex3D>();

        private GLTexture sliderPathTexture;

        public BufferedSliderDrawable(MContext c) {
            super(c);
            updateTexture();
            setAlpha(0);
        }

        public void updateTexture() {
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

            if (sliderPathTexture != null) sliderPathTexture.delete();
            sliderPathTexture = GLTexture.create(bmp);
        }

        public void setSliderPathTexture(GLTexture sliderPathTexture) {
            this.sliderPathTexture = sliderPathTexture;
        }

        public GLTexture getSliderPathTexture() {
            return sliderPathTexture;
        }

        @Override
        public void draw(BaseCanvas canvas) {

            super.draw(canvas);
            //canvas.drawTexture(GLTexture.White,getArea(),Color4.gray(0.3f),0.5f);
            //Vec2 startPoint=getArea().getTopLeft();
			/*
			TextPrinter printer=new TextPrinter(startPoint.x,startPoint.y+30);
			printer.setTextSize(30);
			printer.printString(slider.getSliderIndex()+"");
			printer.draw(canvas);
			*/
        }

        @Override
        protected void drawContent(BaseCanvas canvas) {

            /*canvas.drawColor(Color4.Alpha);
            canvas.clearBuffer();
            LegacyDrawLinePath<Texture3DBatch> d = new LegacyDrawLinePath<Texture3DBatch>(
                    getSliderPath()
                            .cutPath(
                                    (float) (slider.getPixelLength() * getProgress1()),
                                    (float) (slider.getPixelLength() * getProgress2())
                            )
            );
            batch.clear();
            d.addToBatch(batch);
            GLWrapped.depthTest.save();
            GLWrapped.depthTest.set(true);
            canvas.getBlendSetting().save();
            canvas.getBlendSetting().setEnable(false);
            canvas.drawTexture3DBatch(batch, sliderPathTexture, 1, Color4.White);
            canvas.getBlendSetting().restore();
            GLWrapped.depthTest.restore();*/
        }
    }
}
