package com.edplan.nso.ruleset.std.objects.v2;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.NsoException;
import com.edplan.nso.parser.ParseException;
import com.edplan.nso.ruleset.base.object.StdFormatHitObject;
import com.edplan.nso.ruleset.std.objects.StdPath;
import com.edplan.superutils.U;
import com.edplan.superutils.classes.strings.StringSplitter;

public abstract class StdHitObject extends StdFormatHitObject{



    protected static StdPath parsePath(Vec2 startPoint, StringSplitter spl) throws ParseException {
        try {
            StdPath p = new StdPath();
            p.addControlPoint(startPoint);
            spl = new StringSplitter(spl.next(), "\\|");
            p.setType(StdPath.Type.forName(spl.next()));
            while (spl.hasNext()) {
                p.addControlPoint(parseVec2FF(spl.next()));
            }
            return p;
        } catch (Exception e) {
            throw new ParseException("err parsing sliderpath. msg: " + e.getMessage(), e);
        }
    }

    protected static Vec2 parseVec2FF(String s) {
        String[] sp = s.split(":");
        return new Vec2(Float.parseFloat(sp[0]), Float.parseFloat(sp[1]));
    }
}
