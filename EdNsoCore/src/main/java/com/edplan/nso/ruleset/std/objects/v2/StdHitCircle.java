package com.edplan.nso.ruleset.std.objects.v2;

import com.edplan.nso.parser.ParseException;
import com.edplan.superutils.classes.strings.StringSplitter;

public class StdHitCircle extends StdHitObject {

    public static final int TYPE_MASK = 1;

    @Override
    public void parseCustomDatas(StringSplitter spl) throws ParseException {
        //标准note不带任何附加属性
    }
}
