package com.edplan.nso.ruleset.base.game.judge;

@FunctionalInterface
public interface TimeChecker {
    boolean checkTime(double time);
}
