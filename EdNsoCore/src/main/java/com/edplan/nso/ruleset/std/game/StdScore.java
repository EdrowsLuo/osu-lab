package com.edplan.nso.ruleset.std.game;

import com.edplan.nso.ruleset.base.game.Score;

public class StdScore implements Score {

    @Override
    public long getScore() {
        return 0;
    }

    @Override
    public double getAcc() {
        return 0;
    }

    @Override
    public Rank getRank() {
        return null;
    }

    @Override
    public int getCombo() {
        return 0;
    }

}
