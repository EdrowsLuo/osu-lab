package com.edplan.framework.ui.animation;

import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;

import java.util.List;
import java.util.ArrayList;

import com.edplan.framework.ui.animation.interpolate.IInterpolator;
import com.edplan.framework.ui.animation.interpolate.ValueInterpolator;
import com.edplan.framework.interfaces.Setter;
import com.edplan.framework.interfaces.InvokeSetter;

public class QueryAnimation<T, V> extends BasePreciseAnimation {
    private List<AnimNode> nodes = new ArrayList<AnimNode>();

    private ValueInterpolator<V> interpolator;

    private InvokeSetter<T, V> setter;

    private boolean alwaysInitial = true;

    private AnimNode currentNode;

    private double initialOffset;

    private T target;

    public QueryAnimation(T target, double initialOffset, ValueInterpolator<V> interpolator, InvokeSetter<T, V> setter, boolean alwaysInitial) {
        this.target = target;
        this.interpolator = interpolator;
        this.setter = setter;
        this.alwaysInitial = alwaysInitial;
        this.initialOffset = initialOffset;
        setStartTime(initialOffset);
    }

    public void transform(V value, double duration, Easing easing) {
        if (hasNode()) {
            AnimNode pre = getEndNode();
            AnimNode next = new AnimNode(value, pre.endTime, duration, easing);
            pre.next = next;
            next.pre = pre;
            nodes.add(next);
        } else {
            nodes.add(new AnimNode(value, initialOffset, duration, easing));
        }
    }

    public void transform(V value, double startTime, double duration, Easing easing) {
        if (hasNode()) {
            AnimNode pre = getEndNode();
            if (startTime < pre.endTime) {
                double offset = pre.endTime - startTime;
                startTime += offset;
                duration -= offset;
            }
            AnimNode next = new AnimNode(value, startTime, duration, easing);
            pre.next = next;
            next.pre = pre;
            nodes.add(next);
        } else {
            nodes.add(new AnimNode(value, initialOffset, 0, Easing.None));
            transform(value, startTime, duration, easing);
        }
    }

    public void skip(double time) {
        transform(getEndNode().value, time, Easing.None);
    }

    public double currentTime() {
        return initialOffset + getProgressTime();
    }

    @Override
    protected void postTime(double deltaTime, double progressTime) {

        if (hasNode()) {
            if (currentNode == null) {
                currentNode = nodes.get(0);
            }
            while (currentNode.next != null && currentNode.next.startTime < currentTime()) {
                currentNode = currentNode.next;
            }
            currentNode.apply(currentTime());
        }
    }

    @Override
    protected void seekToTime(double progressTime) {

        currentNode = nodes.get(0);
        while (currentNode.next != null && currentNode.next.startTime < currentTime()) {
            currentNode = currentNode.next;
        }
        currentNode.apply(currentTime());
    }

    public boolean hasNode() {
        return nodes.size() != 0;
    }

    // obj(i).start<= time <obj(i).end=obj(i+1).start
    private int search(double time) {
        if (time < 0) {
            return -1;
        } else if (time > getDuration()) {
            return nodes.size();
        } else {
            int i = 0;
            while (time < nodes.get(i).startTime) {
                i++;
                if (i == nodes.size()) {
                    return i;
                }
            }
            while (nodes.get(i).endTime <= time) {
                i++;
                if (i == nodes.size()) {
                    return i;
                }
            }
            return i;
        }
    }

    @Override
    public double getDuration() {

        return (nodes.size() > 0) ? (getEndNode().endTime - getStartTimeAtTimeline()) : 0;
    }

    public double getEndNodeTime() {
        return (hasNode()) ? getEndNode().endTime : 0;
    }

    public AnimNode getEndNode() {
        return nodes.size() > 0 ? nodes.get(nodes.size() - 1) : null;
    }

    @Override
    public void dispos() {

        super.dispos();
        for (AnimNode n : nodes) n.dispos();
        nodes.clear();
    }

    public class AnimNode {
        //这些时间均为ProgressTime
        V value;
        Easing easing;
        double endTime;
        double duration;
        double startTime;

        public AnimNode next;
        public AnimNode pre;

        public AnimNode(V value, double startTime, double duration, Easing easing) {
            this.value = value;
            this.duration = duration;
            this.startTime = startTime;
            this.endTime = startTime + duration;
            this.easing = easing;
        }

        public double getEndTime() {
            return endTime;
        }

        public V interplate(double time) {
            if (pre == null) {
                return value;
            } else {
                return interpolator.applyInterplate(pre.value, value, time, easing);
            }
        }

        public void apply(double progressTime) {
            double p = Math.min(1, Math.max(0, (duration == 0) ? 1 : ((progressTime - startTime) / duration)));
            setter.invoke(target, interplate(p));
        }

        public void dispos() {
            pre = null;
            next = null;
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (AnimNode n : nodes) {
            sb.append("->(v:")
                    .append(n.value)
                    .append(",s:")
                    .append(n.startTime)
                    .append(",d:")
                    .append(n.duration)
                    .append(",e:")
                    .append(n.endTime)
                    .append(")\n");
        }
        return sb.toString();
    }

}
