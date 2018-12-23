package com.edplan.nso.ruleset.base.game.judge;

@FunctionalInterface
public interface PositionChecker {
    boolean checkPosition(float x, float y);
}
