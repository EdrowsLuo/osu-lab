package com.edplan.nso.ruleset.std.playing;

import android.util.Log;

import com.edplan.framework.MContext;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.nso.filepart.PartDifficulty;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.base.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.std.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.StdHitCircle;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.objects.StdSlider;
import com.edplan.nso.ruleset.std.objects.StdSpinner;
import com.edplan.nso.ruleset.std.playing.controlpoint.ControlPoints;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdFollowpoint;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitCircle;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdSlider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StdPlayingBeatmap extends PlayingBeatmap {
    private StdBeatmap beatmap;

    private MContext context;

    private List<DrawableStdHitObject> drawableHitObjects;

    private List<DrawableStdHitObject> connectionObjs;

    private ControlPoints controlPoints;

    public StdPlayingBeatmap(MContext context, StdBeatmap beatmap, PreciseTimeline timeline, OsuSkin skin) {
        super(skin, timeline);
        this.context = context;
        this.beatmap = beatmap;
        loadControlPoints();
        calDrawables();
    }

    public void loadControlPoints() {
        controlPoints = new ControlPoints();
        controlPoints.load(getBeatmap().getTimingPoints().getTimingPoints());
    }

    public void calDrawables() {
        int objCount = getHitObjects().size();
        drawableHitObjects = new ArrayList<DrawableStdHitObject>(objCount);
        DrawableStdHitObject dobj = null;
        int count = 0;

        if (!getHitObjects().get(0).isNewCombo()) {
            Log.w("err-beatmap", "Beatmap's first GameObject must be a new combo");
            getHitObjects().get(0).setIsNewCombo(true);
        }

        for (StdHitObject obj : getHitObjects()) {
            count++;
            dobj = createDrawableHitObject(obj);
            //Log.v("load-bmp",count+"/"+getHitObjects().size());
            drawableHitObjects.add(dobj);
        }
        //Log.v("Slider-size",maxSliderArray+"");
        Collections.sort(drawableHitObjects, new Comparator<DrawableStdHitObject>() {
            @Override
            public int compare(DrawableStdHitObject p1, DrawableStdHitObject p2) {

                return (int) Math.signum(p1.getShowTime() - p2.getShowTime());
            }
        });
        int comboIndex = 1;
        for (DrawableStdHitObject obj : drawableHitObjects) {
            if (obj.getHitObject().isNewCombo()) {
                comboIndex = 1;
            } else {
                comboIndex++;
            }
            obj.setComboIndex(comboIndex);
            obj.applyDefault(this);
        }
        applyStack(drawableHitObjects, beatmap);
        connectionObjs = new ArrayList<DrawableStdHitObject>(objCount);
        DrawableStdHitObject pre = (objCount != 0) ? drawableHitObjects.get(0) : null;
        DrawableStdHitObject now;
        DrawableStdFollowpoint followpoint;
        for (int i = 1; i < objCount; i++) {
            now = drawableHitObjects.get(i);
            if (!now.getHitObject().isNewCombo()) {
                followpoint = new DrawableStdFollowpoint(getContext(), pre, now);
                connectionObjs.add(followpoint);
            }
            pre = now;
        }
        Collections.sort(connectionObjs, new Comparator<DrawableStdHitObject>() {
            @Override
            public int compare(DrawableStdHitObject p1, DrawableStdHitObject p2) {

                return (int) Math.signum(p1.getShowTime() - p2.getShowTime());
            }
        });

        for (DrawableStdHitObject obj : connectionObjs) {
            obj.applyDefault(this);
        }
    }

    public StdBeatmap getBeatmap() {
        return beatmap;
    }

    public MContext getContext() {
        return context;
    }

    public List<StdHitObject> getHitObjects() {
        return getBeatmap().getHitObjects().getHitObjectList();
    }

    public List<DrawableStdHitObject> getDrawableHitObjects() {
        return drawableHitObjects;
    }

    public List<DrawableStdHitObject> getDrawableConnections() {
        return connectionObjs;
    }

    public DrawableStdHitObject createDrawableHitObject(StdHitObject obj) {
        DrawableStdHitObject dobj = null;
        if (obj instanceof StdSlider) {
            dobj = new DrawableStdSlider(getContext(), (StdSlider) obj);
            //Log.v("sld","create sld");
        } else {
            dobj = new DrawableStdHitCircle(getContext(), obj);
        }
        if (obj.isNewCombo()) {
            getSkin().comboColorGenerater.skipColors(obj.getComboColorsSkip());
            dobj.setAccentColor(getSkin().comboColorGenerater.nextColor());
        } else {
            dobj.setAccentColor(getSkin().comboColorGenerater.currentColor());
        }
        return dobj;
    }

    @Override
    public PartDifficulty getDifficulty() {

        return getBeatmap().getDifficulty();
    }

    @Override
    public ControlPoints getControlPoints() {

        return controlPoints;
    }

    static final int stack_distance = 3;

    public static List<DrawableStdHitObject> applyStack(List<DrawableStdHitObject> hitObjects, StdBeatmap beatmap) {
        // Reset stacking
        for (int i = 0; i <= hitObjects.size() - 1; i++) {
            hitObjects.get(i).setStackHeight(0);
        }

        // Extend the end index to include objects they are stacked on
        int extendedEndIndex = hitObjects.size() - 1;
        for (int i = hitObjects.size() - 1; i >= 0; i--) {
            int stackBaseIndex = i;
            for (int n = stackBaseIndex + 1; n < hitObjects.size(); n++) {
                DrawableStdHitObject stackBaseObject = hitObjects.get(stackBaseIndex);
                if (stackBaseObject.getHitObject() instanceof StdSpinner) break;

                DrawableStdHitObject objectN = hitObjects.get(n);
                if (objectN.getHitObject() instanceof StdSpinner)
                    continue;

                double endTime = stackBaseObject.getObjStartTime(); //(stackBaseObject as IHasEndTime)?.EndTime ?? stackBaseObject.getObjStartTime();
                double stackThreshold = objectN.getTimePreempt() * beatmap.getGeneral().getStackLeniency();

                if (objectN.getObjStartTime() - endTime > stackThreshold)
                    //We are no longer within stacking range of the next object.
                    break;

                if (Vec2.length(stackBaseObject.getStartPoint(), objectN.getStartPoint()) < stack_distance ||
                        stackBaseObject.getHitObject() instanceof StdSlider && Vec2.length(stackBaseObject.getEndPoint(), objectN.getStartPoint()) < stack_distance) {
                    stackBaseIndex = n;

                    // GameObjects after the specified update range haven't been reset yet
                    objectN.setStackHeight(0);
                }
            }

            if (stackBaseIndex > extendedEndIndex) {
                extendedEndIndex = stackBaseIndex;
                if (extendedEndIndex == hitObjects.size() - 1)
                    break;
            }
        }

        //Reverse pass for stack calculation.
        int extendedStartIndex = 0;
        for (int i = extendedEndIndex; i > 0; i--) {
            int n = i;
            /* We should check every note which has not yet got a stack.
             * Consider the case we have two interwound stacks and this will make sense.
             *
             * o <-1      o <-2
             *  o <-3      o <-4
             *
             * We first process starting from 4 and handle 2,
             * then we come backwards on the i loop iteration until we reach 3 and handle 1.
             * 2 and 1 will be ignored in the i loop because they already have a stack value.
             */


            DrawableStdHitObject objectI = hitObjects.get(i);
            if (objectI.getStackHeight() != 0 || objectI.getHitObject() instanceof StdSpinner)
                continue;

            double stackThreshold = objectI.getTimePreempt() * beatmap.getGeneral().getStackLeniency();

            /* If this object is a hitcircle, then we enter this "special" case.
             * It either ends with a stack of hitcircles only, or a stack of hitcircles that are underneath a slider.
             * Any other case is handled by the "is Slider" code below this.
             */

            if (objectI.getHitObject() instanceof StdHitCircle) {
                while (--n >= 0) {
                    DrawableStdHitObject objectN = hitObjects.get(n);
                    if (objectN.getHitObject() instanceof StdSpinner) continue;

                    double endTime = objectN.getObjPredictedEndTime();
                    if (objectI.getObjStartTime() - endTime > stackThreshold)
                        //We are no longer within stacking range of the previous object.
                        break;

                    // GameObjects before the specified update range haven't been reset yet
                    if (n < extendedStartIndex) {
                        objectN.setStackHeight(0);
                        extendedStartIndex = n;
                    }

                    /* This is a special case where hticircles are moved DOWN and RIGHT (negative stacking) if they are under the *last* slider in a stacked pattern.
                     *    o==o <- slider is at original location
                     *        o <- hitCircle has stack of -1
                     *         o <- hitCircle has stack of -2
                     */

                    if (objectN.getHitObject() instanceof StdSlider && Vec2.length(objectN.getEndPoint(), objectI.getStartPoint()) < stack_distance) {
                        float offset = objectI.getStackHeight() - objectN.getStackHeight() + 1;
                        for (int j = n + 1; j <= i; j++) {
                            //For each object which was declared under this slider, we will offset it to appear *below* the slider end (rather than above).
                            DrawableStdHitObject objectJ = hitObjects.get(j);
                            if (Vec2.length(objectN.getEndPoint(), objectJ.getStartPoint()) < stack_distance)
                                objectJ.setStackHeight(objectJ.getStackHeight() - offset);
                        }

                        //We have hit a slider.  We should restart calculation using this as the new base.
                        //Breaking here will mean that the slider still has StackCount of 0, so will be handled in the i-outer-loop.
                        break;
                    }

                    if (Vec2.length(objectN.getStartPoint(), objectI.getStartPoint()) < stack_distance) {
                        //Keep processing as if there are no sliders.  If we come across a slider, this gets cancelled out.
                        //NOTE: Sliders with start positions stacking are a special case that is also handled here.

                        objectN.setStackHeight(objectI.getStackHeight() + 1);
                        objectI = objectN;
                    }
                }
            } else if (objectI.getHitObject() instanceof StdSlider) {
                /* We have hit the first slider in a possible stack.
                 * From this point on, we ALWAYS stack positive regardless.
                 */

                while (--n >= 0) {
                    DrawableStdHitObject objectN = hitObjects.get(n);
                    if (objectN.getHitObject() instanceof StdSpinner) continue;

                    if (objectI.getObjStartTime() - objectN.getObjStartTime() > stackThreshold)
                        //We are no longer within stacking range of the previous object.
                        break;

                    if (Vec2.length(objectN.getEndPoint(), objectI.getStartPoint()) < stack_distance) {
                        objectN.setStackHeight(objectI.getStackHeight() + 1);
                        objectI = objectN;
                    }
                }
            }
        }
        for (DrawableStdHitObject obj : hitObjects)
            obj.onApplyStackHeight();
        return hitObjects;
    }
}
