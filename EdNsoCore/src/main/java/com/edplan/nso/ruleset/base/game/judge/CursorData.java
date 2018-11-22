package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.base.game.World;

public abstract class CursorData extends JudgeData{

    public static class Cursor {

        private int index;

        private double downTime;

        private boolean isDown;

        private double eventTime;

        private Vec2 startPos = new Vec2();

        private Vec2 currentPos = new Vec2();

        private boolean holdingDownEvent = false;

        private boolean holdingUpEvent = false;

        private boolean holdingMoveEvent = false;

    }
}
