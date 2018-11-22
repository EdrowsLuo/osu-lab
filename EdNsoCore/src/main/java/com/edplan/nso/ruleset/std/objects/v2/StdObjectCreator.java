package com.edplan.nso.ruleset.std.objects.v2;

import com.edplan.nso.ruleset.base.beatmap.parser.StdFormatObjectParser;

public class StdObjectCreator implements StdFormatObjectParser.ObjectCreator<StdGameObject> {

    private static StdObjectCreator creator = new StdObjectCreator();

    public static StdObjectCreator getInstance() {
        return creator;
    }

    @Override
    public StdGameObject create(int type) {
        if ((type & StdGameCircle.TYPE_MASK) > 0) {
            return new StdGameCircle();
        } else if ((type & StdSlider.TYPE_MASK) > 0) {
            return new StdSlider();
        } else if ((type & StdSpinner.TYPE_MASK) > 0) {
            return new StdSpinner();
        }
        return null;
    }
}
