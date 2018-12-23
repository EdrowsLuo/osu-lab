package com.edplan.nso.ruleset.base.game.judge;

public class HitWindow {

    public static TimeChecker interval(double time, double offset) {
        return time1 -> Math.abs(time - time1) < offset;
    }

}
