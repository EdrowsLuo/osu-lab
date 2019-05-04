package com.edplan.nso.ruleset.std.game.v2;

import com.edplan.nso.ruleset.base.game.judge.AreaHitObject;
import com.edplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edplan.nso.ruleset.std.objects.v2.direct.DirectStdCircle;

public class WorkingStdCircle extends WorkingStdGameObject {

    private DirectStdCircle circle;

    private CirclePiece circlePiece;

    private AreaHitObject areaHitObject;

    public WorkingStdCircle(DirectStdCircle circle) {
        this.circle = circle;
    }

    @Override
    public void applyToGameField(StdGameField field) {

    }

}
