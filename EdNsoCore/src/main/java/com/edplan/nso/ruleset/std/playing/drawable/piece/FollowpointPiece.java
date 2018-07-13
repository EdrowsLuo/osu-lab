package com.edplan.nso.ruleset.std.playing.drawable.piece;

import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.MContext;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.utils.MLog;
import com.edplan.framework.graphics.opengl.objs.Vertex3D;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class FollowpointPiece extends BasePiece {
    private GLTexture followpoint;

    private Vec2 p1 = new Vec2();

    private Vec2 p2 = new Vec2();

    private Vec2 offCircleP1;

    private Vec2 offCircleP2;

    private boolean needDraw = true;

    private float progress = 0;

    private Color4 color1 = Color4.rgba(1, 1, 1, 1);

    private Color4 color2 = Color4.rgba(1, 1, 1, 0);

    private float baseWidth = 4;

    public FollowpointPiece(MContext c, PreciseTimeline t, Vec2 p1, Vec2 p2) {
        super(c, t);
        this.p1.set(p1);
        this.p2.set(p2);
        paint.setStrokeWidth(baseWidth);
        //paint.setColorMixRate(1);
    }

    public void calOffCirclePoints() {
        float dis = Vec2.length(p1, p2);
        if (dis <= getBaseSize() * 2) {
            needDraw = false;
        } else {
            offCircleP1 = Vec2.onLineLength(p1, p2, getBaseSize());
            offCircleP2 = Vec2.onLineLength(p2, p1, getBaseSize());
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
        calOffCirclePoints();
    }

    public float getProgress() {
        return progress;
    }

    public void setColor1(Color4 color1) {
        this.color1.set(color1);
    }

    public Color4 getColor1() {
        return color1;
    }

    public void setColor2(Color4 color2) {
        this.color2.set(color2);
    }

    public Color4 getColor2() {
        return color2;
    }

    @Override
    public void setBaseSize(float baseSize) {

        super.setBaseSize(baseSize);
        paint.setStrokeWidth(baseWidth * getBaseSize() / BasePiece.DEF_SIZE);
        calOffCirclePoints();
    }

    @Override
    public void setAccentColor(Color4 accentColor) {

        super.setAccentColor(accentColor);
    }

    @Override
    public void setSkin(OsuSkin skin) {

        super.setSkin(skin);
        followpoint = skin.followpoint.getRes();
    }

    @Override
    public void draw(BaseCanvas canvas) {

        if (needDraw) {
            Vertex3D[] v = canvas.createLineRectVertex(offCircleP1, Vec2.onLine(offCircleP1, offCircleP2, progress), paint.getStrokeWidth(), color1);
            v[2].setColor(color2);
            v[3].setColor(color2);
            Vertex3D[] o = canvas.createLineRectVertex(p1, p2, paint.getStrokeWidth(), Color4.Alpha);
            canvas.clearTmpColorBatch();
            canvas.addToColorBatch(canvas.rectToTriangles(new Vertex3D[]{o[0], o[1], v[0], v[1]}), paint);
            canvas.addToColorBatch(canvas.rectToTriangles(v), paint);
            canvas.addToColorBatch(canvas.rectToTriangles(new Vertex3D[]{v[3], v[2], o[3], o[2]}), paint);
            canvas.postColorBatch(paint);
        }
    }
}
