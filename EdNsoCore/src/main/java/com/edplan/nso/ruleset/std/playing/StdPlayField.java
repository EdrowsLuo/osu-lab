package com.edplan.nso.ruleset.std.playing;

import android.util.Log;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.nso.ruleset.base.playing.PlayField;
import com.edplan.nso.ruleset.base.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdSlider;
import com.edplan.nso.ruleset.std.playing.drawable.interfaces.IHasApproachCircle;
import com.edplan.nso.ruleset.std.playing.drawable.interfaces.IHasBackgroundDrawable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StdPlayField extends PlayField {
    private FieldDrawables backgroundLayerExtension = new FieldDrawables();

    private FieldDrawables drawableHitObjects = new FieldDrawables();

    private FieldDrawables connectionDrawables = new FieldDrawables();

    private PreciseTimeline timeline;

    //private approachCircleLayer;

    //private connectionLayer;

    public StdPlayField(MContext context, PreciseTimeline timeline) {
        super(context);
        this.timeline = timeline;
    }

    public int objectInFieldCount() {
        return drawableHitObjects.getObjectsInField().size();
    }

    public List<DrawableStdHitObject> getDrawableHitObjects() {
        return drawableHitObjects.getObjects();
    }

    public int getFirstHitObjectIndex() {
        return drawableHitObjects.getFirstObjectIndex();
    }

    @Override
    public void applyBeatmap(PlayingBeatmap res) {

        Log.v("beatmap-data", res.getDifficulty().toString());
        if (res instanceof StdPlayingBeatmap) {
            StdPlayingBeatmap beatmap = (StdPlayingBeatmap) res;
            drawableHitObjects.setObjects(beatmap.getDrawableHitObjects());
            connectionDrawables.setObjects(beatmap.getDrawableConnections());
            Log.v("test-datas", "Connections: " + connectionDrawables.getObjects().size());
        } else {
            throw new IllegalArgumentException("you can only apply a StdPlayingBeatmap to StdPlayField");
        }
    }

    protected void drawBackgroundLayer(BaseCanvas canvas) {
        DrawableStdHitObject obj;
        for (int i = drawableHitObjects.getObjectsInField().size() - 1; i >= 0; i--) {
            obj = drawableHitObjects.getObjectsInField().get(i);
            if (obj instanceof IHasBackgroundDrawable) {
                ((IHasBackgroundDrawable) obj).getBackgroundDrawable().draw(canvas);
            }
        }
    }

    protected void drawContentLayer(BaseCanvas canvas) {
        DrawableStdHitObject obj;
        canvas.enablePost();
        for (int i = drawableHitObjects.getObjectsInField().size() - 1; i >= 0; i--) {
            obj = drawableHitObjects.getObjectsInField().get(i);
            obj.draw(canvas);
            //if(obj instanceof IHasApproachCircle){
            //	((IHasApproachCircle)obj).getApproachCircle().draw(canvas);
            //}
        }
        canvas.disablePost();
    }

    protected void drawConnectionLayer(BaseCanvas canvas) {
        DrawableStdHitObject obj;
        //canvas.enablePost();
        for (int i = connectionDrawables.getObjectsInField().size() - 1; i >= 0; i--) {
            obj = connectionDrawables.getObjectsInField().get(i);
            obj.draw(canvas);
        }
        //canvas.disablePost();
    }

    protected void drawApproachCircleLayer(BaseCanvas canvas) {
        DrawableStdHitObject obj;
        canvas.enablePost();
        for (int i = drawableHitObjects.getObjectsInField().size() - 1; i >= 0; i--) {
            obj = drawableHitObjects.getObjectsInField().get(i);
            if (obj instanceof IHasApproachCircle) {
                ((IHasApproachCircle) obj).getApproachCircle().draw(canvas);
            }
        }
        canvas.disablePost();
    }

    private void drawTestLayer(BaseCanvas canvas) {

        List<DrawableStdHitObject> l = drawableHitObjects.getObjectsInField();
        GLPaint paint = new GLPaint();
        //paint.setColorMixRate(1);
        paint.setStrokeWidth(2f);
        paint.setVaryingColor(Color4.rgba(1, 0, 0, 1));
        for (DrawableStdHitObject obj : l) {
            if (obj instanceof DrawableStdSlider) {
                DrawableStdSlider sld = (DrawableStdSlider) obj;
                LinePath path = sld.getPath();
                float[] list = new float[(path.size() / 2) * 4];
                for (int i = 0; i < (path.size() / 2) * 2; i++) {
                    list[i * 2] = path.get(i).x;
                    list[i * 2 + 1] = path.get(i).y;
                }
                canvas.drawLines(list, paint);
            }
        }
    }

    @Override
    public void draw(BaseCanvas canvas) {

        int curTime = (int) timeline.frameTime();
        //添加新的物件
        drawableHitObjects.prepareToDraw(curTime);
        connectionDrawables.prepareToDraw(curTime);

        drawBackgroundLayer(canvas);
        drawConnectionLayer(canvas);
        drawContentLayer(canvas);
        drawApproachCircleLayer(canvas);
    }

    public class FieldDrawables {
        private List<DrawableStdHitObject> drawableHitObjects;

        //private IEnum<DrawableStdHitObject> hitObjects;

        private List<DrawableStdHitObject> hitObjectsInField = new ArrayList<DrawableStdHitObject>();

        private int savedIndex = 0;

        public FieldDrawables() {

        }

        public int getFirstObjectIndex() {
            return savedIndex;
        }

        public void setObjectsInField(List<DrawableStdHitObject> hitObjectsInField) {
            this.hitObjectsInField = hitObjectsInField;
        }

        public List<DrawableStdHitObject> getObjectsInField() {
            return hitObjectsInField;
        }

        public void setObjects(List<DrawableStdHitObject> drawableHitObjects) {
            this.drawableHitObjects = drawableHitObjects;
        }

        public List<DrawableStdHitObject> getObjects() {
            return drawableHitObjects;
        }

        public void prepareToDraw(int curTime) {
            pullNewAdded(curTime);
            deleteFinished();
        }

        private void showHitObject(DrawableStdHitObject obj) {
            hitObjectsInField.add(obj);
            obj.onShow();
        }

        private void removeHitObject(DrawableStdHitObject obj) {
            obj.onFinish();
        }

        //private DrawableStdHitObject currentObj;
        private void pullNewAdded(int curTime) {
            DrawableStdHitObject obj;
            for (; savedIndex < drawableHitObjects.size(); savedIndex++) {
                obj = drawableHitObjects.get(savedIndex);
                if (obj.getShowTime() <= curTime) {
                    showHitObject(obj);
                } else {
                    break;
                }
            }
        }

        protected void deleteFinished() {
            Iterator<DrawableStdHitObject> iter = hitObjectsInField.iterator();
            DrawableStdHitObject obj;
            while (iter.hasNext()) {
                obj = iter.next();
                if (obj.isFinished()) {
                    removeHitObject(obj);
                    iter.remove();
                }
            }
        }

    }
}
