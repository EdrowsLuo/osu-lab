package com.edplan.nso.ruleset.std.objects.v2;

import com.edplan.nso.parser.ParseException;
import com.edplan.superutils.classes.strings.StringSplitter;

public class StdSpinner extends StdHitObject {

    public static final int TYPE_MASK = 8;

    private int endTime;

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public void parseCustomDatas(StringSplitter spl) throws ParseException {
        endTime = Integer.parseInt(spl.next());
    }
}
