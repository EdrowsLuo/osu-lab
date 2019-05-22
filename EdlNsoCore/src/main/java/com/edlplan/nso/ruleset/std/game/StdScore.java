package com.edlplan.nso.ruleset.std.game;

import com.edlplan.nso.difficulty.DifficultyUtil;
import com.edlplan.framework.utils.functionality.Collector;
import com.edlplan.nso.ruleset.base.game.Score;

public class StdScore implements Score {

    private DifficultyUtil.BuiltDifficultyHelper difficultyHelper;

    private int combo;

    private Collector<Integer,Integer> maxCombo = Collector.max(0);



    public void applyHitResult(HitResult result) {
        switch (result.hitType) {
            case NoteHit:onNoteHit(result);break;
            case SliderStartHit:onSliderStartHit(result);break;
            case SliderTickHit:onSliderTickHit(result);break;
        }
        maxCombo.update(combo);
    }

    private void onComboBreak() {

    }

    protected void onNoteHit(HitResult result) {
        if (result.isTimeOut) {
            if (combo > 0) {
                combo = 0;
                onComboBreak();
            }
            return;
        }
        double offset = Math.abs(result.offset);
        if (offset <= difficultyHelper.hitWindowFor300()) {

        } else if (offset <= difficultyHelper.hitWindowFor100()) {

        } else if (offset <= difficultyHelper.hitWindowFor50()) {

        }
    }

    protected void onSliderStartHit(HitResult result) {

    }

    protected void onSliderTickHit(HitResult result) {

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
        return combo;
    }

    public static class HitResult {

        public HitType hitType;

        public double sourceTime;

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
