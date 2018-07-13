package com.edplan.nso.storyboard.elements;

import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.interfaces.FloatInvokeSetter;
import com.edplan.framework.interfaces.InvokeSetter;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.QueryAnimation;
import com.edplan.framework.ui.animation.interpolate.Color4Interpolator;
import com.edplan.framework.ui.animation.interpolate.InvalidInterpolator;
import com.edplan.framework.ui.animation.interpolate.RawFloatInterpolator;
import com.edplan.framework.ui.animation.interpolate.ValueInterpolator;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.nso.storyboard.elements.drawable.ADrawableStoryboardElement;
import com.edplan.nso.storyboard.elements.drawable.BaseDrawableSprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoryboardSprite implements IStoryboardElements {
    private List<CommandLoop> loops = new ArrayList<CommandLoop>();
    private List<CommandTrigger> triggers = new ArrayList<CommandTrigger>();

    private CommandTimeLineGroup commandTimeLineGroup = new CommandTimeLineGroup();

    private String path;
    private Vec2 initialPosition;
    private Anchor anchor;

    public StringBuilder rawData = new StringBuilder();

    public StoryboardSprite(String path, Anchor anchor, Vec2 initialPosition) {
        this.path = new String(path);
        this.initialPosition = initialPosition;
        this.anchor = anchor;
    }

    public void getScale() {

    }

    public void setInitialPosition(Vec2 initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Vec2 getInitialPosition() {
        return initialPosition;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public double getStartTime() {
        double v1 = commandTimeLineGroup.hasCommands() ? commandTimeLineGroup.getCommandsStartTime() : Double.MAX_VALUE;
        double v2 = Double.MAX_VALUE;
        for (CommandLoop l : loops) {
            if (l.hasCommands()) {
                v2 = Math.min(v2, l.getStartTime());
            }
        }
        return Math.min(v1, v2);
    }

    public double getEndTime() {
        double v1 = commandTimeLineGroup.hasCommands() ? commandTimeLineGroup.getCommandsEndTime() : Double.MIN_VALUE;
        double v2 = Double.MIN_VALUE;
        for (CommandLoop l : loops) {
            if (l.hasCommands()) {
                v2 = Math.max(v2, l.getEndTime());
            }
        }
        return Math.max(v1, v2);
    }

    public double getDuration() {
        return getEndTime() - getStartTime();
    }

    public CommandTimeLineGroup getCommandTimeLineGroup() {
        return commandTimeLineGroup;
    }

    public boolean hasCommands() {
        if (commandTimeLineGroup.hasCommands()) {
            return true;
        } else {
            for (CommandLoop c : loops) {
                if (c.hasCommands()) return true;
            }
            return false;
        }
    }

    public CommandLoop addLoop(double startTime, int loopCount) {
        CommandLoop loop = new CommandLoop(startTime, loopCount);
        loops.add(loop);
        return loop;
    }

    public CommandTrigger addTrigger(String triggerName, double startTime, double endTime, int groupNumber) {
        CommandTrigger c = new CommandTrigger(triggerName, startTime, endTime, groupNumber);
        triggers.add(c);
        return c;
    }

    private static Comparator<TypedCommand> comparator = new Comparator<TypedCommand>() {
        @Override
        public int compare(TypedCommand p1, TypedCommand p2) {

            return (int) Math.signum(p1.getStartTime() - p2.getStartTime());
        }
    };

    private <T> void applyCommands(BaseDrawableSprite sprite, List<TypedCommand<T>> command, ValueInterpolator<T> interpolator, InvokeSetter<BaseDrawableSprite, T> setter, T startValue, boolean keepInEnding) {
        double offset = sprite.getStartTime();
        double obj_offset = getStartTime();
        QueryAnimation<BaseDrawableSprite, T> anim = new QueryAnimation<BaseDrawableSprite, T>(sprite, obj_offset, interpolator, setter, true);
        Collections.sort(command, comparator);
        if (command.size() == 0) return;
		
		/*
		T startV=(startValue!=null)?startValue:command.get(0).getStartValue();
		setter.invoke(sprite,startV);
		anim.transform(startV,getStartTime(),0,Easing.None);
		*/

        TypedCommand<T> tmp = command.get(0);
        setter.invoke(sprite, tmp.getStartValue());
        anim.transform(tmp.getStartValue(), offset - obj_offset, Easing.None);
        for (TypedCommand<T> c : command) {
            anim.transform(c.getStartValue(), c.getStartTime(), 0, c.getEasing());
            anim.transform(c.getEndValue(), c.getStartTime(), c.getDuration(), c.getEasing());
        }
        if (!keepInEnding) {
            anim.transform(startValue, anim.getEndNode().getEndTime(), 0, Easing.None);
        }
        sprite.addAnimation(anim, setter);
    }

    private void applyRawFloatCommands(BaseDrawableSprite sprite, List<TypedCommand<Float>> command, RawFloatInterpolator interpolator, FloatInvokeSetter<BaseDrawableSprite> setter, float startValue, boolean keepInEnding) {
        double offset = sprite.getStartTime();
        double obj_offset = getStartTime();
        FloatQueryAnimation<BaseDrawableSprite> anim = new FloatQueryAnimation<BaseDrawableSprite>(sprite, obj_offset, interpolator, setter, true);
        Collections.sort(command, comparator);
        if (command.size() == 0) return;

		/*
		 T startV=(startValue!=null)?startValue:command.get(0).getStartValue();
		 setter.invoke(sprite,startV);
		 anim.transform(startV,getStartTime(),0,Easing.None);
		 */

        TypedCommand<Float> tmp = command.get(0);
        setter.invoke(sprite, tmp.getStartValue());
        anim.transform(tmp.getStartValue(), offset - obj_offset, Easing.None);
        float preValue = Float.MAX_VALUE;
        for (TypedCommand<Float> c : command) {
            if (!FMath.allmostEqual(c.getStartValue(), preValue, 0.0001f)) {
                anim.transform(c.getStartValue(), c.getStartTime(), 0, Easing.None);
            }
            preValue = c.getEndValue();
            anim.transform(c.getEndValue(), c.getStartTime(), c.getDuration(), c.getEasing());
        }
        if (!keepInEnding) {
            anim.transform(startValue, anim.getEndNode().getEndTime(), 0, Easing.None);
        }
        sprite.addAnimation(anim, setter);
    }

    public static int tmpI = 0;

    public <T> List<TypedCommand<T>> getCommands(CommandTimelineSelecter<T> selecter) {
        List<TypedCommand<T>> list = new ArrayList<TypedCommand<T>>();
        list.addAll(commandTimeLineGroup.getCommands(selecter, 0));
        for (CommandLoop l : loops) {
            list.addAll(l.getCommands(selecter, 0));
        }
        return list;
    }

    public String getInitialPath() {
        return path;
    }

    public void initialTexture(BaseDrawableSprite sprite, PlayingStoryboard storyboard) {
        sprite.setTexture(storyboard.getTexturePool().getTexture(getInitialPath()));
    }

    @Override
    public void onApply(ADrawableStoryboardElement ele, PlayingStoryboard storyboard) {

        if (ele == null) return;

        BaseDrawableSprite sprite = (BaseDrawableSprite) ele;
        initialTexture(sprite, storyboard);
        applyRawFloatCommands(sprite, getCommands(Selecters.SX), RawFloatInterpolator.Instance, BaseDrawableSprite.XRaw, initialPosition.x, true);
        applyRawFloatCommands(sprite, getCommands(Selecters.SY), RawFloatInterpolator.Instance, BaseDrawableSprite.YRaw, initialPosition.y, true);
        applyRawFloatCommands(sprite, getCommands(Selecters.SScaleX), RawFloatInterpolator.Instance, BaseDrawableSprite.ScaleXRaw, 1, true);
        applyRawFloatCommands(sprite, getCommands(Selecters.SScaleY), RawFloatInterpolator.Instance, BaseDrawableSprite.ScaleYRaw, 1, true);
        applyRawFloatCommands(sprite, getCommands(Selecters.SRotation), RawFloatInterpolator.Instance, BaseDrawableSprite.RotationRaw, 0f, true);
        applyCommands(sprite, getCommands(Selecters.SColour), Color4Interpolator.Instance, BaseDrawableSprite.Color, null, true);
        applyRawFloatCommands(sprite, getCommands(Selecters.SAlpha), RawFloatInterpolator.Instance, BaseDrawableSprite.AlphaRaw, 1, true);
        applyCommands(sprite, getCommands(Selecters.SBlendType), InvalidInterpolator.ForBlendType, BaseDrawableSprite.Blend, BlendType.Normal, false);
        applyCommands(sprite, getCommands(Selecters.SFlipH), InvalidInterpolator.ForBoolean, BaseDrawableSprite.FlipH, false, false);
        applyCommands(sprite, getCommands(Selecters.SFlipV), InvalidInterpolator.ForBoolean, BaseDrawableSprite.FlipV, false, false);
    }

    @Override
    public boolean isDrawable() {

        return hasCommands();
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {

        return path;
    }

    @Override
    public String[] getTexturePaths() {

        return new String[]{getPath()};
    }

    @Override
    public BaseDrawableSprite createDrawable(PlayingStoryboard storyboard) {

        return new BaseDrawableSprite(storyboard, this);
    }
}
