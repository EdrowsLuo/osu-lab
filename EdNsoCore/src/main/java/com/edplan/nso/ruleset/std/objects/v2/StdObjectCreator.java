package com.edplan.nso.ruleset.std.objects.v2;

import com.edplan.nso.ruleset.base.beatmap.parser.StdFormatObjectParser;

public class StdObjectCreator implements StdFormatObjectParser.ObjectCreator<StdGameObject> {

    private static StdObjectCreator creator = new StdObjectCreator();

    public static StdObjectCreator getInstance() {
        return creator;
    }

    @Override
    public StdGameObject create(int type) {
        if ((type & StdCircle.TYPE_MASK) > 0) {
            return new StdCircle();
        } else if ((type & StdSlider.TYPE_MASK) > 0) {
            return new StdSlider();
        } else if ((type & StdSpinner.TYPE_MASK) > 0) {
            return new StdSpinner();
        }
        return null;
    }
}
