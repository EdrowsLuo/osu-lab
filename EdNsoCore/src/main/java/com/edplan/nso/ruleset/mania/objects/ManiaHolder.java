package com.edplan.nso.ruleset.mania.objects;

import com.edplan.nso.ruleset.std.objects.StdHitObjectType;

public class ManiaHolder extends ManiaHitObject {
    private int endTime;

    public ManiaHolder() {

    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public StdHitObjectType getResType() {

        return StdHitObjectType.ManiaHolder;
    }
}
