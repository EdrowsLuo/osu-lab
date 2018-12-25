package com.edplan.nso.ruleset.base.game.judge;

public class HitWindow implements TimeChecker{

    private double start, end;

    public HitWindow(double start, double end) {
        this.start = start;
        this.end = end;
    }

    public static HitWindow interval(double time, double offset) {
        return new HitWindow(time - offset, time + offset);
    }

    public double getEnd() {
        return end;
    }

    public double getStart() {
        return start;
    }

    @Override
    public boolean checkTime(double time) {
        return start <= time && time <= end;
    }
}
