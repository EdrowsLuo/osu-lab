package com.edplan.framework.ui.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.ui.drawable.operation.ITexturePoster;
import com.edplan.framework.ui.drawable.operation.TestPoster;

public class BitmapDrawable extends EdDrawable {
    public int[] timelist = new int[80];

    public int deltaTime;

    public String text = "aaaaa";

    private Bitmap content;

    private GLTexture contentTexture;

    private ITexturePoster poster = new TestPoster();

    public BitmapDrawable(MContext c) {
        super(c);
        content = Bitmap.createBitmap(300, 600, Bitmap.Config.ARGB_8888);
    }

    public void drawBitmap(Canvas c) {
        c.drawColor(Color.WHITE);
        TextPaint p = new TextPaint();
        p.setTextSize(100);
        p.setAlpha(255);
        p.setARGB(255, 0, 0, 0);
        StaticLayout lay = new StaticLayout(
                text,
                p,
                c.getWidth(),
                Layout.Alignment.ALIGN_NORMAL,
                1.01f,
                0,
                false
        );
        lay.draw(c);


        Paint tp = new Paint();
        Paint fp = new Paint();
        tp.setARGB(255, 255, 255, 255);
        tp.setTextSize(30);
        tp.setStrokeWidth(7);
        fp.setARGB(255, 0, 0, 0);
        c.save();
        c.clipRect(c.getWidth() - 200, c.getHeight() - 600, c.getWidth(), c.getHeight());
        c.translate(c.getWidth() - 200, c.getHeight() - 600);
        c.drawARGB(255, 0, 0, 0);
        c.drawText("DeltaTime: " + deltaTime, 10, 30, tp);
        c.drawLine(10, 50, 10 + 18 * 6, 50, tp);
        c.drawLine(10, 60, 10 + deltaTime * 6, 60, tp);
        for (int i = timelist.length - 1; i > 0; i--) {
            timelist[i] = timelist[i - 1];
        }
        timelist[0] = deltaTime;
        for (int i = 0; i < timelist.length; i++) {
            c.drawLine(10, 55 + 5 * i, 10 + timelist[i] * 6, 55 + 5 * i, tp);
        }
        float avg = 0;
        for (int t : timelist) {
            avg += t;
        }
        avg /= timelist.length;
        tp.setStrokeWidth(3);
        tp.setARGB(255, 255, 0, 0);
        c.drawLine(10 + avg * 6, 40, 10 + avg * 6, 60 + 5 * timelist.length, tp);
        c.restore();
    }

    private void loadToGLTexture() {
        if (contentTexture != null) {
            contentTexture.delete();
        }
        contentTexture = GLTexture.create(content);
    }

    @Override
    public void draw(BaseCanvas canvas) {

        drawBitmap(new Canvas(content));
        loadToGLTexture();
        poster.drawTexture(canvas, contentTexture);
    }
}
