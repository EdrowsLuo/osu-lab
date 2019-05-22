package com.edlplan.nso.ruleset.base.game;

public interface Score {

    long getScore();

    double getAcc();

    Rank getRank();

    int getCombo();

    enum Rank {
        SS,
        S,
        A,
        B,
        C,
        D,
        F
    }

}
