package com.edplan.nso.ruleset.std.game;

import com.edplan.nso.ruleset.base.game.Score;

public class StdScore implements Score {



    public void applyHitResult(HitResult result) {

    }

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

    public static class HitResult {

        public double sourseTime;

        public double offset;

        public boolean isTimeOut = false;

        public Object hitExtra;

    }

    public enum HitType {
        NoteHit,
        SliderStartHit,
        SliderTickHit,
        SliderReverseHit,
        SliderEndHit,
        SpinnerProgress,
        SpinnerEnd
    }

    public enum HitLevel {
        H300,
        H100,
        H50,
        MISS,
    }

}
