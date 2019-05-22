package com.edlplan.nso.storyboard.elements;

import com.edlplan.framework.ui.animation.Easing;

public class TypedCommand<T> implements ICommand {
    private Easing easing;
    private double startTime;
    private double endTime;

    private T startValue;
    private T endValue;

    public TypedCommand(Easing e, double startTime, double endTime, T startValue, T endValue) {
        this.easing = e;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public void setStartValue(T startValue) {
        this.startValue = startValue;
    }

    public T getStartValue() {
        return startValue;
    }

    public void setEndValue(T endValue) {
        this.endValue = endValue;
    }

    public T getEndValue() {
        return endValue;
    }

    @Override
    public Easing getEasing() {

        return easing;
    }

    @Override
    public void setEasing(Easing easing) {

        this.easing = easing;
    }

    @Override
    public double getStartTime() {

        return startTime;
    }

    @Override
    public void setStartTime(double startTime) {

        this.startTime = startTime;
    }

    @Override
    public double getEndTime() {

        return endTime;
    }

    @Override
    public void setEndTime(double endTime) {

        this.endTime = endTime;
    }

    @Override
    public double getDuration() {

        return endTime - startTime;
    }

    @Override
    public int compareTo(ICommand other) {

        int result = (int) Math.signum(other.getStartTime() - getStartTime());
        if (result != 0) {
            return result;
        } else {
            return (int) Math.signum(other.getEndTime() - getEndTime());
        }
    }
}
