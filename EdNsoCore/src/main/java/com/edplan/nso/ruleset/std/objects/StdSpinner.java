package com.edplan.nso.ruleset.std.objects;

public class StdSpinner extends StdHitObject {
    private int endTime;


    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public StdHitObjectType getResType() {

        return StdHitObjectType.Spinner;
    }
}
