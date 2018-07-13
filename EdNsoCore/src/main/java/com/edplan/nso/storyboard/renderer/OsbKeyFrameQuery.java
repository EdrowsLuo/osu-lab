package com.edplan.nso.storyboard.renderer;

import com.edplan.nso.storyboard.elements.TypedCommand;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

import com.edplan.nso.storyboard.renderer.OsbRenderer.*;

import java.util.ArrayList;

import com.edplan.framework.ui.animation.Easing;

import java.util.Collections;

public class OsbKeyFrameQuery<T> {
    public static Comparator<TypedCommand> COMPARATOR = new Comparator<TypedCommand>() {
        @Override
        public int compare(TypedCommand p1, TypedCommand p2) {
            return (int) Math.signum(p1.getStartTime() - p2.getStartTime());
        }
    };

    private final OsbKeyFrame<T>[] frames;
    private final float[] nextFrameStartTime;
    private final int stopFlag;
    private int currentFrameIndex;
    private OsbKeyFrame<T> currentFrame;
    private final IMainFrameHandler<T> handler;

    public OsbKeyFrameQuery(List<TypedCommand<T>> commands, IMainFrameHandler<T> handler, double expandMinTime, T defaultValue, boolean initialWithStartValue, boolean continueLink) {
        this.handler = handler;
        Collections.sort(commands, COMPARATOR);
        final List<TypedCommand<T>> list = new ArrayList<TypedCommand<T>>();
        if (commands.size() == 0) {
            list.add(new TypedCommand<T>(Easing.None, expandMinTime, expandMinTime, defaultValue, defaultValue));
        } else {
            if (!initialWithStartValue && !continueLink && expandMinTime < commands.get(0).getStartTime()) {
                list.add(new TypedCommand<T>(Easing.None, expandMinTime, commands.get(0).getStartTime(), defaultValue, defaultValue));
            }
            double currentTime = Math.min(expandMinTime, commands.get(0).getStartTime());
            T preEndValue = defaultValue;
            for (int i = 0; i < commands.size(); i++) {
                final TypedCommand<T> command = commands.get(i);
                if (continueLink && currentTime < command.getStartTime()) {
                    list.add(new TypedCommand<T>(Easing.None, currentTime, command.getStartTime(), preEndValue, command.getStartValue()));
                }
                list.add(command);
                currentTime = command.getEndTime();
                preEndValue = command.getEndValue();
            }
            if (continueLink)
                list.add(new TypedCommand<T>(Easing.None, currentTime, currentTime, preEndValue, preEndValue));
        }
        Collections.fill(commands, null);
        commands = list;
        frames = new OsbKeyFrame[commands.size()];
        nextFrameStartTime = new float[frames.length];
        for (int i = 0; i < frames.length; i++) {
            final TypedCommand<T> command = commands.get(i);
            frames[i] = new OsbKeyFrame<T>(command.getStartValue(), command.getEndValue(), (float) command.getStartTime(), (float) command.getDuration(), command.getEasing());
        }
        for (int i = 1; i < frames.length; i++) {
            nextFrameStartTime[i - 1] = frames[i].startTime;
        }
        nextFrameStartTime[frames.length - 1] = Float.MAX_VALUE;
        currentFrameIndex = 0;
        currentFrame = frames[0];
        stopFlag = frames.length - 1;
        Collections.fill(list, null);
    }

    public T calLazyCurrentValue(double time) {
        if (time < currentFrame.startTime) {
            return currentFrame.startValue;
        } else if (time > currentFrame.endTime || currentFrame.duration == 0) {
            return currentFrame.endValue;
        } else {
            return currentFrame.startValue;
        }
    }

    private boolean hasChange;

    public void update(double time) {
        hasChange = false;
        while (nextFrameStartTime[currentFrameIndex] < time && currentFrameIndex < stopFlag) {
            currentFrame = frames[++currentFrameIndex];
            hasChange = true;
        }
        if (hasChange) {
            handler.update(currentFrame.startValue, currentFrame.endValue, currentFrame.startTime, currentFrame.duration, currentFrame.easing);
        }
    }

    public void dispos() {
        Arrays.fill(frames, null);
    }
}
