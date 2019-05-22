package com.edlplan.nso.ruleset.std.objects.v2.raw;

import com.edlplan.nso.parser.ParseException;
import com.edlplan.framework.utils.advance.StringSplitter;

public class StdCircle extends StdGameObject {

    public static final int TYPE_MASK = 1;

    @Override
    public void parseCustomDatas(StringSplitter spl) throws ParseException {
        //标准note不带任何附加属性
    }
}
