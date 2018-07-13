package com.edplan.nso.ruleset.mania.objects;

import com.edplan.nso.ruleset.base.object.HitObject;
import com.edplan.nso.ruleset.mania.objects.ManiaHolder;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.objects.StdHitObjects;

public class ManiaHitObjects extends StdHitObjects {
    @Override
    public void addHitObject(HitObject r) {

        if (r instanceof StdHitObject) {
            StdHitObject t = (StdHitObject) r;
            switch (t.getResType()) {
                case Circle:
                    super.addHitObject(ManiaNote.toManiaNote(t));
                    return;
                case ManiaHolder:
                    super.addHitObject(t);
                    return;
                default:
                    return;
            }
        }
    }
}
