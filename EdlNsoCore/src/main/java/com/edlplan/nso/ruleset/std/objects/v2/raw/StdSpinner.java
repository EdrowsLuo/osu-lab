package com.edlplan.nso.ruleset.std.objects.v2.raw;

import com.edlplan.nso.parser.ParseException;
import com.edlplan.framework.utils.advance.StringSplitter;

public class StdSpinner extends StdGameObject {

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
